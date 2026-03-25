package com.zor07.lastsave.service.bot

import com.zor07.lastsave.service.github.GitHubOAuthService
import com.zor07.lastsave.service.notification.NotificationService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CommandHandler(
    private val studentService: StudentService,
    private val gitHubOAuthService: GitHubOAuthService,
    private val notificationService: NotificationService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handle(event: TelegramMessageEvent) {
        val message = event.message
        if (message.isCommand && message.text == "/start") {
            handleStart(message.chatId)
        }
    }

    private fun handleStart(chatId: Long) {
        val existing = studentService.findByChatId(chatId)
        if (existing != null) {
            logger.info("Student {} already registered, chatId={}", existing.id, chatId)
            notificationService.sendText(existing, "Ты уже зарегистрирован, GitHub username: ${existing.githubUsername}.")
            return
        }
        val url = gitHubOAuthService.registrationUrl(chatId)
        notificationService.sendText(chatId, "Привет! Авторизуйся через GitHub: $url")
    }
}
