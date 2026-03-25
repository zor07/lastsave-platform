package com.zor07.lastsave.controller.review

import com.zor07.lastsave.dto.review.PrReviewRequest
import com.zor07.lastsave.service.review.PrReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pr-review")
class PrReviewController(
    private val prReviewService: PrReviewService,
) {

    @PostMapping
    fun review(@RequestBody request: PrReviewRequest): ResponseEntity<Void> {
        prReviewService.handlePrReview(request.githubUsername, request.prUrl, request.diff)
        return ResponseEntity.ok().build()
    }
}
