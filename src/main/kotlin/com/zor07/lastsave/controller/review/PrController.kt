package com.zor07.lastsave.controller.review

import com.zor07.lastsave.dto.review.PrReportRequest
import com.zor07.lastsave.service.ci.CiService
import com.zor07.lastsave.service.review.PrReportService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pr")
class PrController(
    private val prReportService: PrReportService,
    private val ciService: CiService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/report")
    fun report(@RequestBody request: PrReportRequest): ResponseEntity<Void> {
        logger.info("PR report: githubUsername=${request.githubUsername}, prUrl=${request.prUrl}, passed=${request.passed}, failedTests=${request.failedTests.size}, diffLength=${request.diff.length}")
        prReportService.handlePrReport(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/test-tag")
    fun getTestTag(@RequestParam githubUsername: String): ResponseEntity<String> {
        val tag = ciService.getTestTag(githubUsername) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(tag)
    }
}
