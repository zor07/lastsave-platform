package com.zor07.lastsave.service.auth

import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.github.GitHubOAuthClient
import com.zor07.lastsave.service.github.GitHubService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GitHubOAuthServiceImpl(
    private val studentService: StudentService,
    private val gitHubService: GitHubService,
    private val telegramBot: TelegramBot,
    private val gitHubOAuthClient: GitHubOAuthClient,
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
            githubName = user.name,
        )

        val repoUrl = gitHubService.createRepoFromTemplate(student.githubUsername)
        gitHubService.addCollaborator(repoUrl.substringAfterLast("/"), student.githubUsername)
        telegramBot.sendRepoLink(chatId, repoUrl)

        logger.info("Registered student {} for chat {} and repo {}", student.id, chatId, repoUrl)
    }
}
