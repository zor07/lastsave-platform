package com.zor07.lastsave.service.review

import com.zor07.lastsave.model.ReviewJob
import com.zor07.lastsave.repository.ReviewJobRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewJobProcessor(
    private val reviewJobRepository: ReviewJobRepository,
    private val prReviewService: PrReviewService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun process(job: ReviewJob) {
        reviewJobRepository.markProcessing(job.id)
        try {
            prReviewService.handlePrReview(job.githubUsername, job.prUrl, job.diff)
            reviewJobRepository.markDone(job.id)
            logger.info("ReviewJobProcessor: job {} done", job.id)
        } catch (e: Exception) {
            reviewJobRepository.markFailed(job.id)
            logger.error("ReviewJobProcessor: job {} failed (retry {}): {}", job.id, job.retryCount + 1, e.message)
        }
    }
}
