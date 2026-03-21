package com.zor07.lastsave.service.auth

import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.github.GitHubOAuthClient
import com.zor07.lastsave.service.progress.BlockProgressService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GitHubOAuthServiceImpl(
    private val studentService: StudentService,
    private val gitHubOAuthClient: GitHubOAuthClient,
    private val blockProgressService: BlockProgressService,
    private val telegramBot: TelegramBot,
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

        blockProgressService.startFirstBlockIfNeeded(student)?.let {
            telegramBot.sendRepoLink(chatId, it.repoUrl, it.blockTitle)
        }

        logger.info("Registered student {} for chat {}", student.id, chatId)
    }
}
