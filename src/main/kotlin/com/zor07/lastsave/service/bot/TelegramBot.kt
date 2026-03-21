package com.zor07.lastsave.service.bot

import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import jakarta.annotation.PostConstruct

@Component
class TelegramBot(
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
        val message = update?.message ?: return
        if (message.isCommand && message.text == "/start") {
            val chatId = message.chatId
            val url =
                "https://github.com/login/oauth/authorize?client_id=$githubClientId&state=$chatId&redirect_uri=${appBaseUrl}/auth/github/callback"
            sendMessage(chatId, "Привет! Авторизуйся через GitHub: $url")
        }
    }

    fun sendRepoLink(chatId: Long, repoUrl: String) {
        sendMessage(chatId, "Твой репозиторий: $repoUrl")
    }

    private fun sendMessage(chatId: Long, text: String) {
        try {
            execute(SendMessage(chatId.toString(), text))
        } catch (ex: Exception) {
            logger.error("Failed to send message", ex)
        }
    }
}
