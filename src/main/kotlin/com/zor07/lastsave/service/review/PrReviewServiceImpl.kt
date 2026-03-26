package com.zor07.lastsave.service.review

import com.zor07.lastsave.model.MessageLog
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.service.notification.NotificationService
import com.zor07.lastsave.service.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PrReviewServiceImpl(
    private val studentService: StudentService,
    private val studentProgressRepository: StudentProgressRepository,
    private val messageLogRepository: MessageLogRepository,
    private val aiReviewService: AiReviewService,
    private val notificationService: NotificationService,
) : PrReviewService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private data class ParsedPrReview(val student: Student, val log: MessageLog)

    @Transactional
    override fun handlePrReview(githubUsername: String, prUrl: String, diff: String) {
        try {
            val (student, log) = parsePrReview(githubUsername)

            messageLogRepository.markPrReceived(log.id)
            logger.info("PR received for student {} in section {}", student.id, log.messageId)

            val review = aiReviewService.review(diff)

            val header = "Ревью твоего PR $prUrl\n\n"
            val chunks = splitIntoChunks(header + review, 2000)
            chunks.forEach { notificationService.sendText(student, it) }
            logger.info("PR review sent to student {}", student.id)
        } catch (e: Exception) {
            logger.error("Failed to process PR review for githubUsername={}: {}", githubUsername, e.message)
        }
    }

    private fun splitIntoChunks(text: String, chunkSize: Int): List<String> {
        val chunks = mutableListOf<String>()
        var start = 0
        while (start < text.length) {
            chunks.add(text.substring(start, minOf(start + chunkSize, text.length)))
            start += chunkSize
        }
        return chunks
    }

    private fun parsePrReview(githubUsername: String): ParsedPrReview {
        val student = studentService.findByGithubUsername(githubUsername)
            ?: throw IllegalStateException("No student found for githubUsername=$githubUsername")
        val progress = studentProgressRepository.findActiveByStudentId(student.id)
            ?: throw IllegalStateException("No active progress for student ${student.id}")
        val log = messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)
            ?: throw IllegalStateException("No message log found for student ${student.id} in section ${progress.sectionId}")

        return ParsedPrReview(student, log)
    }
}
