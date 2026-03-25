package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.enums.WaitFor
import com.zor07.lastsave.model.Message
import com.zor07.lastsave.model.NewMessageLog
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.service.bot.TelegramBot
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class MessageDispatchServiceImpl(
    private val studentProgressRepository: StudentProgressRepository,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val telegramBot: TelegramBot,
) : MessageDispatchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun advanceIfPossible(student: Student) {
        val progress = studentProgressRepository.findActiveByStudentId(student.id) ?: run {
            logger.info("Student {} has no active progress, skipping", student.id)
            return
        }
        logger.info("Student {} is on section {}", student.id, progress.sectionId)

        val lastLog = messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)

        if (lastLog == null) {
            logger.info("Student {} has no messages sent in section {} yet, sending first", student.id, progress.sectionId)
            val first = messageRepository.findFirstInSection(progress.sectionId) ?: run {
                logger.info("Section {} has no messages, skipping", progress.sectionId)
                return
            }
            send(student, first)
            return
        }

        val lastMessage = messageRepository.findById(lastLog.messageId) ?: return

        val ready = when (lastMessage.waitFor) {
            WaitFor.NOTHING  -> true
            WaitFor.CALLBACK -> lastLog.callbackReceivedAt != null
            WaitFor.PR       -> false
        }

        if (!ready) {
            logger.info("Student {} waiting for {} on message {}", student.id, lastMessage.waitFor, lastMessage.id)
            return
        }

        val next = messageRepository.findNextInSection(progress.sectionId, lastMessage.order) ?: run {
            logger.info("Student {} has received all messages in section {}, waiting for section advance", student.id, progress.sectionId)
            return
        }
        send(student, next)
    }

    private fun send(student: Student, message: Message) {
        if (message.callbackText != null) {
            telegramBot.sendMessageWithButton(
                chatId = student.telegramChatId,
                text = message.text,
                buttonText = message.callbackText,
                callbackData = message.id.toString(),
            )
        } else {
            telegramBot.sendTextMessage(student.telegramChatId, message.text)
        }
        messageLogRepository.save(NewMessageLog(message.id, student.id, LocalDateTime.now()))
        logger.info("Sent message {} (waitFor={}) to student {}", message.id, message.waitFor, student.id)
    }
}
