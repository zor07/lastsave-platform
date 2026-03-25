package com.zor07.lastsave.service.review

interface AiReviewService {
    fun review(diff: String): String
}
