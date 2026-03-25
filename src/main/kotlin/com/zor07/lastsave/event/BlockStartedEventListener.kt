package com.zor07.lastsave.event

import com.zor07.lastsave.service.github.GitHubService
import com.zor07.lastsave.service.notification.NotificationService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BlockStartedEventListener(
    private val gitHubService: GitHubService,
    private val notificationService: NotificationService,
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onBlockStarted(event: BlockStartedEvent) {
        val repoUrl = gitHubService.createRepoFromTemplate(event.templateRepoName, event.student.githubUsername)
        notificationService.sendText(event.student, "Новый спринт! Сосредоточимся на «${event.blockTitle}». Твой репозиторий для работы: $repoUrl")
    }
}
