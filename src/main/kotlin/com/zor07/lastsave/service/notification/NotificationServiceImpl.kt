package com.zor07.lastsave.service.notification

import com.zor07.lastsave.model.Message
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.service.bot.TelegramBot
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    private val telegramBot: TelegramBot,
) : NotificationService {

    override fun sendText(chatId: Long, text: String) {
        telegramBot.sendTextMessage(chatId, text)
    }

    override fun sendText(student: Student, text: String) {
        telegramBot.sendTextMessage(student.telegramChatId, text)
    }

    override fun sendMessage(student: Student, message: Message) {
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
    }
}
