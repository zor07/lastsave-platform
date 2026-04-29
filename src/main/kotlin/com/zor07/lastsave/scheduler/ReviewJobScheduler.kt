package com.zor07.lastsave.scheduler

import com.zor07.lastsave.repository.ReviewJobRepository
import com.zor07.lastsave.service.review.ReviewJobProcessor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ReviewJobScheduler(
    private val reviewJobRepository: ReviewJobRepository,
    private val reviewJobProcessor: ReviewJobProcessor,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 10_000)
    fun processJobs() {
        val jobs = reviewJobRepository.findPending()
        if (jobs.isEmpty()) return

        logger.info("ReviewJobScheduler: processing {} pending jobs", jobs.size)
        jobs.forEach { reviewJobProcessor.process(it) }
    }
}
