package com.zor07.lastsave.event

import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.github.GitHubService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BlockStartedEventListener(
    private val gitHubService: GitHubService,
    private val telegramBot: TelegramBot,
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onBlockStarted(event: BlockStartedEvent) {
        val repoUrl = gitHubService.createRepoFromTemplate(event.templateRepoName, event.student.githubUsername)
        telegramBot.sendTextMessage(event.student.telegramChatId, "Новый спринт! Сосредоточимся на «${event.blockTitle}». Твой репозиторий для работы: $repoUrl")
    }
}
