package com.zor07.lastsave.service.bot

import com.zor07.lastsave.service.messaging.MessageCallbackService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import jakarta.annotation.PostConstruct

@Component
class TelegramBot(
    private val messageCallbackService: MessageCallbackService,
    private val studentService: StudentService,
    @Value("\${telegram.bot.token}") private val token: String,
    @Value("\${telegram.bot.username}") private val username: String,
    @Value("\${github.client-id}") private val githubClientId: String,
    @Value("\${app.base-url}") private val appBaseUrl: String,
) : TelegramLongPollingBot(token) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername(): String = username

    override fun onUpdateReceived(update: Update?) {
        val safeUpdate = requireNotNull(update) { "Update must not be null" }
        safeUpdate.message?.let { handleMessage(it) }
        safeUpdate.callbackQuery?.let { callback ->
            val chatId = requireNotNull(callback.message?.chatId) { "Callback chatId is required" }
            val messageId = requireNotNull(callback.data?.toLongOrNull()) { "Callback data must contain message id" }
            messageCallbackService.handleCallback(chatId, messageId)
            answerCallback(callback.id, "Спасибо! Продолжаем.")
        }
    }

    fun sendRepoLink(chatId: Long, repoUrl: String, blockTitle: String) {
        sendTextMessage(chatId, "Новый спринт! Сосредоточимся на «$blockTitle». Твой репозиторий для работы: $repoUrl")
    }

    fun sendTextMessage(chatId: Long, text: String) {
        try {
            execute(SendMessage(chatId.toString(), text))
        } catch (ex: Exception) {
            logger.error("Failed to send message", ex)
        }
    }

    fun sendMessageWithButton(chatId: Long, text: String, buttonText: String, callbackData: String) {
        val keyboard = InlineKeyboardMarkup(listOf(listOf(InlineKeyboardButton(buttonText).apply { this.callbackData = callbackData })))
        try {
            val message = SendMessage(chatId.toString(), text).apply {
                replyMarkup = keyboard
            }
            execute(message)
        } catch (ex: Exception) {
            logger.error("Failed to send message with button", ex)
        }
    }

    fun answerCallback(callbackQueryId: String, text: String? = null) {
        try {
            execute(AnswerCallbackQuery(callbackQueryId).apply { this.text = text })
        } catch (ex: Exception) {
            logger.error("Failed to answer callback", ex)
        }
    }

    private fun handleMessage(message: org.telegram.telegrambots.meta.api.objects.Message) {
        if (message.isCommand && message.text == "/start") {
            val chatId = message.chatId
            val existing = studentService.findByTelegramChatId(chatId)
            if (existing != null) {
                sendTextMessage(chatId, "Ты уже зарегистрирован, GitHub username: ${existing.githubUsername}.")
                return
            }
            val url =
                "https://github.com/login/oauth/authorize?client_id=$githubClientId&state=$chatId&redirect_uri=${appBaseUrl}/auth/github/callback"
            sendTextMessage(chatId, "Привет! Авторизуйся через GitHub: $url")
        }
    }
}
