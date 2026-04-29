package com.zor07.lastsave.model

import java.time.LocalDateTime

enum class ReviewJobStatus {
    PENDING, PROCESSING, DONE, FAILED
}

data class ReviewJob(
    val id: Long,
    val githubUsername: String,
    val prUrl: String,
    val diff: String,
    val status: ReviewJobStatus,
    val retryCount: Int,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime?,
)
