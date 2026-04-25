package com.zor07.lastsave.service.bot

import com.zor07.lastsave.model.Message
import com.zor07.lastsave.model.MessageLog
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.service.notification.NotificationService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackHandler(
    private val studentService: StudentService,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val notificationService: NotificationService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private data class ParsedCallback(val student: Student, val message: Message, val log: MessageLog)

    @EventListener
    @Transactional
    fun handle(event: TelegramCallbackEvent) {
        try {
            val (student, message, log) = parseCallback(event.callbackQuery)

            messageLogRepository.markCallbackReceived(log.id)
            logger.info("Callback received from student {} for message {}", student.id, message.id)

            val telegramMessageId = event.callbackQuery.message?.messageId
            if (telegramMessageId != null) {
                notificationService.removeKeyboard(student, telegramMessageId)
            }
            // next message will arrive on the next scheduler tick
        } catch (e: Exception) {
            logger.error("Failed to process callback: {}", e.message)
        }
    }

    private fun parseCallback(callback: CallbackQuery): ParsedCallback {
        val chatId = callback.message?.chatId
            ?: throw IllegalStateException("Callback has no chatId")
        val messageId = callback.data?.toLongOrNull()
            ?: throw IllegalStateException("Callback from chatId=$chatId has no valid messageId in data")
        val student = studentService.findByChatId(chatId)
            ?: throw IllegalStateException("No student found for chatId=$chatId")
        val message = messageRepository.findById(messageId)
            ?: throw IllegalStateException("Message $messageId not found")
        val log = messageLogRepository.findLastForStudentInSection(student.id, message.sectionId)
            ?: throw IllegalStateException("No message log found for student ${student.id} in section ${message.sectionId}")

        return ParsedCallback(student, message, log)
    }
}
