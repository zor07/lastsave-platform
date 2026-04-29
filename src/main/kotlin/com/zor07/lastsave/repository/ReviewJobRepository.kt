package com.zor07.lastsave.repository

import com.zor07.lastsave.model.ReviewJob
import com.zor07.lastsave.model.ReviewJobStatus
import com.zor07.lastsave.table.ReviewJobsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

private const val MAX_RETRIES = 3

@Repository
class ReviewJobRepository {

    fun save(githubUsername: String, prUrl: String, diff: String) {
        ReviewJobsTable.insert {
            it[ReviewJobsTable.githubUsername] = githubUsername
            it[ReviewJobsTable.prUrl] = prUrl
            it[ReviewJobsTable.diff] = diff
            it[status] = ReviewJobStatus.PENDING.name
            it[retryCount] = 0
            it[createdAt] = LocalDateTime.now()
            it[processedAt] = null
        }
    }

    fun findPending(): List<ReviewJob> =
        ReviewJobsTable.selectAll()
            .where {
                (ReviewJobsTable.status eq ReviewJobStatus.PENDING.name) and
                (ReviewJobsTable.retryCount less MAX_RETRIES)
            }
            .map { it.toReviewJob() }

    fun markProcessing(id: Long) {
        ReviewJobsTable.update({ ReviewJobsTable.id eq id }) {
            it[status] = ReviewJobStatus.PROCESSING.name
        }
    }

    fun markDone(id: Long) {
        ReviewJobsTable.update({ ReviewJobsTable.id eq id }) {
            it[status] = ReviewJobStatus.DONE.name
            it[processedAt] = LocalDateTime.now()
        }
    }

    fun markFailed(id: Long) {
        ReviewJobsTable.update({ ReviewJobsTable.id eq id }) {
            it[status] = ReviewJobStatus.PENDING.name
            it[retryCount] = ReviewJobsTable.retryCount + 1
        }
    }

    private fun ResultRow.toReviewJob() = ReviewJob(
        id = this[ReviewJobsTable.id],
        githubUsername = this[ReviewJobsTable.githubUsername],
        prUrl = this[ReviewJobsTable.prUrl],
        diff = this[ReviewJobsTable.diff],
        status = ReviewJobStatus.valueOf(this[ReviewJobsTable.status]),
        retryCount = this[ReviewJobsTable.retryCount],
        createdAt = this[ReviewJobsTable.createdAt],
        processedAt = this[ReviewJobsTable.processedAt],
    )
}
