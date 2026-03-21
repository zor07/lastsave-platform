package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.Message
import com.zor07.lastsave.entity.MessageLog
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.enums.MessageWaitFor
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.progress.BlockProgressService
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
    private val blockProgressService: BlockProgressService,
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
        val activeProgress = ensureActiveProgress(student) ?: return
        val sectionId = activeProgress.sectionId

        val lastLog = messageLogRepository.findFirstByStudentIdOrderBySentAtDesc(student.id ?: return)
        val lastMessage = lastLog?.let { messageRepository.findById(it.messageId).orElse(null) }

        if (lastLog == null || lastMessage == null || lastMessage.sectionId != sectionId) {
            sendFirstMessageOfSection(sectionId, student)
            return
        }

        if (!canProceed(lastMessage, lastLog, student)) {
            return
        }

        val nextMessage = messageRepository.findFirstBySectionIdAndOrderGreaterThanOrderByOrderAsc(
            lastMessage.sectionId,
            lastMessage.order,
        ) ?: return

        sendMessage(nextMessage, student)
    }

    private fun ensureActiveProgress(student: Student): com.zor07.lastsave.entity.StudentProgress? {
        val active = studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(
            student.id ?: return null,
            StudentProgressStatus.IN_PROGRESS,
        )
        if (active != null) {
            return active
        }
        return blockProgressService.startFirstBlockIfNeeded(student)
    }

    private fun sendFirstMessageOfSection(sectionId: Long, student: Student) {
        val message = messageRepository.findFirstBySectionIdOrderByOrderAsc(sectionId) ?: return
        sendMessage(message, student)
    }

    private fun sendMessage(message: Message, student: Student) {
        val chatId = student.telegramChatId
        val messageId = message.id ?: return
        if (message.waitFor == MessageWaitFor.CALLBACK && !message.callbackText.isNullOrBlank()) {
            telegramBot.sendMessageWithButton(chatId, message.text, message.callbackText, messageId.toString())
        } else {
            telegramBot.sendTextMessage(chatId, message.text)
        }
        messageLogRepository.save(
            MessageLog(
                messageId = messageId,
                studentId = student.id ?: return,
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
