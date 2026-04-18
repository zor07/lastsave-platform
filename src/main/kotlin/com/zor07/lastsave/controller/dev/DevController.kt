package com.zor07.lastsave.controller.dev

import com.zor07.lastsave.scheduler.ProgressScheduler
import com.zor07.lastsave.service.github.GitHubService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
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
