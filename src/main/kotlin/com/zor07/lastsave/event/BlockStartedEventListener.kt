package com.zor07.lastsave.event

import com.zor07.lastsave.model.NewStudentRepo
import com.zor07.lastsave.repository.GitRepositoryRepository
import com.zor07.lastsave.repository.StudentRepoRepository
import com.zor07.lastsave.service.github.GitHubService
import com.zor07.lastsave.service.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime

@Component
class BlockStartedEventListener(
    private val gitHubService: GitHubService,
    private val notificationService: NotificationService,
    private val gitRepositoryRepository: GitRepositoryRepository,
    private val studentRepoRepository: StudentRepoRepository,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onBlockStarted(event: BlockStartedEvent) {
        if (event.gitRepositoryId == null) return

        val existing = studentRepoRepository.findByStudentAndGitRepository(
            event.student.id,
            event.gitRepositoryId,
        )
        if (existing != null) return

        val gitRepository = gitRepositoryRepository.findById(event.gitRepositoryId)
            ?: throw IllegalStateException("GitRepository ${event.gitRepositoryId} not found")

        val repoUrl = gitHubService.createRepoFromTemplate(gitRepository.name, event.student.githubUsername)

        studentRepoRepository.create(NewStudentRepo(
            studentId = event.student.id,
            gitRepositoryId = event.gitRepositoryId,
            repoUrl = repoUrl,
            createdAt = LocalDateTime.now(),
        ))

        try {
            notificationService.sendText(
                event.student,
                "Твой репозиторий для работы: $repoUrl\n\nПрими приглашение стать коллаборатором — оно уже у тебя на GitHub. Без этого задания не получится сдать.",
            )
        } catch (e: Exception) {
            logger.error("Failed to send repo notification to student ${event.student.id}", e)
        }
    }
}
