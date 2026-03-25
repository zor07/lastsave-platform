package com.zor07.lastsave.service.github

import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GitHubOAuthServiceImpl(
    private val studentService: StudentService,
    private val gitHubOAuthClient: GitHubOAuthClient,
    private val telegramBot: TelegramBot,
    @Value("\${github.client-id}") private val githubClientId: String,
    @Value("\${app.base-url}") private val appBaseUrl: String,
) : GitHubOAuthService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun processCallback(code: String, state: String) {
        val chatId = state.toLongOrNull()
            ?: throw IllegalArgumentException("Invalid state")

        val accessToken = gitHubOAuthClient.exchangeCodeForToken(code)
        val user = gitHubOAuthClient.fetchUser(accessToken)

        val student = studentService.registerStudent(
            telegramChatId = chatId,
            githubUsername = user.username,
        )

        telegramBot.sendTextMessage(chatId, "GitHub ок — вижу твой username ${student.githubUsername}.")

        logger.info("Registered student {} for chat {}", student.id, chatId)
    }

    override fun registrationUrl(chatId: Long): String =
        "https://github.com/login/oauth/authorize?client_id=$githubClientId&state=$chatId&redirect_uri=${appBaseUrl}/auth/github/callback"

}
