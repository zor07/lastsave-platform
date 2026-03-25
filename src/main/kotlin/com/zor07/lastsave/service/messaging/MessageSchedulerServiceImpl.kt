package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.Message
import com.zor07.lastsave.entity.MessageLog
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.enums.MessageWaitFor
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.progress.StudentProgressService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageSchedulerServiceImpl(
    private val studentRepository: StudentRepository,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val studentProgressRepository: StudentProgressRepository,
    private val studentProgressService: StudentProgressService,
    private val telegramBot: TelegramBot,
) : MessageSchedulerService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 * * * * *")
    override fun scheduleMessages() {
        studentRepository.findAll().forEach { student ->
            processStudent(student)
        }
    }

    private fun processStudent(student: Student) {
        logger.info("Processing student ${student.id}")
        val activeProgress = studentProgressService.getOrStartProgress(student)
        val sectionId = activeProgress.sectionId

        val studentId = requireNotNull(student.id) { "Student id is required" }
        val lastLog = messageLogRepository.findFirstByStudentIdOrderBySentAtDesc(studentId)
        val lastMessage = lastLog?.let { messageRepository.findById(it.messageId).orElse(null) }

        if (lastLog == null || lastMessage == null || lastMessage.sectionId != sectionId) {
            sendFirstMessageOfSection(sectionId, student)
            return
        }

        if (!canProceed(lastMessage, lastLog, student)) {
            return
        }

        val nextMessage = messageRepository.findNextMessageToSend(
            lastMessage.sectionId,
            lastMessage.order,
        ) ?: throw IllegalStateException("Next message not found for section ${lastMessage.sectionId}")

        sendMessage(nextMessage, student)
    }

    private fun sendFirstMessageOfSection(sectionId: Long, student: Student) {
        val message = messageRepository.findFirstBySectionIdOrderByOrderAsc(sectionId)
            ?: throw IllegalStateException("No messages for section $sectionId")
        sendMessage(message, student)
    }

    private fun sendMessage(message: Message, student: Student) {
        val chatId = student.telegramChatId
        val messageId = requireNotNull(message.id) { "Message id is required" }
        if (message.waitFor == MessageWaitFor.CALLBACK && !message.callbackText.isNullOrBlank()) {
            telegramBot.sendMessageWithButton(chatId, message.text, message.callbackText, messageId.toString())
        } else {
            telegramBot.sendTextMessage(chatId, message.text)
        }
        messageLogRepository.save(
            MessageLog(
                messageId = messageId,
                studentId = requireNotNull(student.id) { "Student id is required" },
                sentAt = LocalDateTime.now(),
                callbackReceivedAt = null,
            ),
        )
        logger.info("Sent message {} to student {}", messageId, student.id)
    }

    private fun canProceed(lastMessage: Message, lastLog: MessageLog, student: Student): Boolean =
        when (lastMessage.waitFor) {
            MessageWaitFor.NOTHING -> true
            MessageWaitFor.CALLBACK -> lastLog.callbackReceivedAt != null
            MessageWaitFor.PR -> {
                val progress = studentProgressRepository.findByStudentIdAndSectionId(
                    student.id!!,
                    lastMessage.sectionId,
                )
                progress?.prUrl != null
            }
        }
}
