package com.zor07.lastsave.service.github

import com.zor07.lastsave.service.notification.NotificationService
import com.zor07.lastsave.service.progress.StudentProgressService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GitHubOAuthServiceImpl(
    private val studentService: StudentService,
    private val studentProgressService: StudentProgressService,
    private val notificationService: NotificationService,
    private val gitHubOAuthClient: GitHubOAuthClient,
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

        notificationService.sendText(student, """
            GitHub подключён, добро пожаловать.

Вот как устроена эта стажировка:

— материал приходит сюда, в чат
— ты изучаешь и нажимаешь Готово
— получаешь задание, делаешь его в репозитории на GitHub
— открываешь Pull Request — я проверяю и даю фидбек
— после фидбека прогресс идёт дальше автоматически

Важно: не забегай вперёд. Даже если тема кажется знакомой — не решай задания из следующих спринтов заранее, это сломает логику автоматических проверок.

Сообщения приходят автоматически раз в минуту, если от тебя не ожидается никакого действия. Если пришло сообщение с кнопкой или ожидается PR — следующее придёт только после твоего ответа.
        """.trimIndent())

        studentProgressService.startProgress(student)

        logger.info("Registered student {} for chat {}", student.id, chatId)
    }

    override fun registrationUrl(chatId: Long): String =
        "https://github.com/login/oauth/authorize?client_id=$githubClientId&state=$chatId&redirect_uri=${appBaseUrl}/auth/github/callback"

}
