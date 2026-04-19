package com.zor07.lastsave.service.review

import com.zor07.lastsave.dto.review.PrReportRequest
import com.zor07.lastsave.service.notification.NotificationService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PrReportServiceImpl(
    private val studentService: StudentService,
    private val notificationService: NotificationService,
    private val prReviewService: PrReviewService,
) : PrReportService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handlePrReport(request: PrReportRequest) {
        val student = studentService.findByGithubUsername(request.githubUsername)
        if (student == null) {
            logger.error("handlePrReport: no student found for githubUsername={}", request.githubUsername)
            return
        }

        if (!request.passed) {
            val list = request.failedTests.joinToString("\n") { "• $it" }
            notificationService.sendText(student, "Тесты упали:\n$list\n\nИсправь и запушь снова.")
            logger.info("handlePrReport: test failures sent to student={}", student.id)
            return
        }

        notificationService.sendText(student, "Тесты прошли успешно, жди ревью кода")
        logger.info("handlePrReport: tests passed for student={}, starting AI review", student.id)
        prReviewService.handlePrReview(request.githubUsername, request.prUrl, request.diff)
    }
}
