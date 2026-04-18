package com.zor07.lastsave.controller.dev

import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepoRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.scheduler.ProgressScheduler
import com.zor07.lastsave.service.github.GitHubService
import com.zor07.lastsave.service.progress.StudentProgressService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dev")
class DevController(
    private val gitHubService: GitHubService,
    private val progressScheduler: ProgressScheduler,
    private val studentRepository: StudentRepository,
    private val studentProgressRepository: StudentProgressRepository,
    private val studentRepoRepository: StudentRepoRepository,
    private val messageLogRepository: MessageLogRepository,
    private val studentProgressService: StudentProgressService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/github/repo")
    fun createRepo(
        @RequestParam githubUsername: String,
        @RequestParam templateName: String,
    ): ResponseEntity<String> {
        logger.info("createRepo: githubUsername={}, templateName={}", githubUsername, templateName)
        val repoUrl = gitHubService.createRepoFromTemplate(templateName, githubUsername)
        logger.info("createRepo: created repoUrl={}", repoUrl)
        return ResponseEntity.ok(repoUrl)
    }

    @Transactional
    @PostMapping("/progress/reset")
    fun resetProgress(@RequestParam studentId: Long): ResponseEntity<Void> {
        logger.info("resetProgress: studentId={}", studentId)
        val student = studentRepository.findById(studentId)
            ?: return ResponseEntity.notFound().build()
        studentRepoRepository.findAllByStudentId(student.id).forEach { repo ->
            val repoName = repo.repoUrl.substringAfterLast("/")
            logger.info("resetProgress: deleting GitHub repo={}", repoName)
            gitHubService.deleteRepo(repoName)
        }
        studentRepoRepository.deleteAllByStudentId(student.id)
        messageLogRepository.deleteAllByStudentId(student.id)
        studentProgressRepository.deleteAllByStudentId(student.id)
        studentProgressService.startProgress(student)
        logger.info("resetProgress: done for student={}", student.id)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/scheduler/tick")
    fun tick(): ResponseEntity<Void> {
        logger.info("Manual scheduler tick triggered")
        progressScheduler.doTick()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/github/repo")
    fun deleteRepo(
        @RequestParam githubUsername: String,
        @RequestParam templateName: String,
    ): ResponseEntity<Void> {
        val repoName = "$templateName-$githubUsername"
        logger.info("deleteRepo: repoName={}", repoName)
        gitHubService.deleteRepo(repoName)
        logger.info("deleteRepo: deleted repoName={}", repoName)
        return ResponseEntity.noContent().build()
    }
}
