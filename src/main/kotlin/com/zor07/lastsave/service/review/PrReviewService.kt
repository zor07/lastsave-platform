package com.zor07.lastsave.service.review

interface PrReviewService {
    fun handlePrReview(githubUsername: String, prUrl: String, diff: String)
}
