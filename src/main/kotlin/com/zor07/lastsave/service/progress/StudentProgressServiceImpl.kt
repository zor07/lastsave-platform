package com.zor07.lastsave.service.progress

import com.zor07.lastsave.enums.ProgressStatus
import com.zor07.lastsave.event.BlockStartedEvent
import com.zor07.lastsave.model.Block
import com.zor07.lastsave.model.NewStudentProgress
import com.zor07.lastsave.model.Section
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.repository.BlockRepository
import com.zor07.lastsave.repository.SectionRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.TopicRepository
import com.zor07.lastsave.service.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class StudentProgressServiceImpl(
    private val studentProgressRepository: StudentProgressRepository,
    private val blockRepository: BlockRepository,
    private val topicRepository: TopicRepository,
    private val sectionRepository: SectionRepository,
    private val notificationService: NotificationService,
    private val eventPublisher: ApplicationEventPublisher,
) : StudentProgressService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun startProgress(student: Student) {
        val firstBlock = blockRepository.findFirst()
            ?: throw IllegalStateException("No blocks found in the course")
        startBlock(student, firstBlock)
    }

    override fun startBlock(student: Student, block: Block) {
        if (studentProgressRepository.findActiveByStudentId(student.id) != null) {
            logger.info("Student {} already has active progress, skipping startBlock", student.id)
            return
        }

        val topic = topicRepository.findFirstInBlock(block.id)
            ?: throw IllegalStateException("No topics found for block ${block.id}")
        val section = sectionRepository.findFirstInTopic(topic.id)
            ?: throw IllegalStateException("No sections found for topic ${topic.id}")

        studentProgressRepository.save(NewStudentProgress(
            studentId = student.id,
            sectionId = section.id,
            status = ProgressStatus.IN_PROGRESS,
            startedAt = LocalDateTime.now(),
        ))

        eventPublisher.publishEvent(BlockStartedEvent(student, block.title, block.gitRepositoryId))
        logger.info("Started block {} for student {}", block.id, student.id)
    }

    override fun completeSectionAndAdvance(student: Student, sectionId: Long) {
        studentProgressRepository.markCompleted(student.id, sectionId)
        logger.info("Student {} completed section {}", student.id, sectionId)

        val currentSection = sectionRepository.findById(sectionId)
            ?: throw IllegalStateException("Section $sectionId not found")

        val nextSection = findNextSectionInBlock(currentSection)
        if (nextSection != null) {
            studentProgressRepository.save(NewStudentProgress(
                studentId = student.id,
                sectionId = nextSection.id,
                status = ProgressStatus.IN_PROGRESS,
                startedAt = LocalDateTime.now(),
            ))
            logger.info("Student {} advanced to section {}", student.id, nextSection.id)
            return
        }

        val currentTopic = topicRepository.findById(currentSection.topicId)
            ?: throw IllegalStateException("Topic ${currentSection.topicId} not found")
        val currentBlock = blockRepository.findById(currentTopic.blockId)
            ?: throw IllegalStateException("Block ${currentTopic.blockId} not found")
        val nextBlock = blockRepository.findNextByOrder(currentBlock.order)

        if (nextBlock != null) {
            startBlock(student, nextBlock)
        } else {
            logger.info("Student {} has completed the course", student.id)
            notificationService.sendText(student, "Поздравляем! Ты прошёл курс целиком.")
        }
    }

    private fun findNextSectionInBlock(currentSection: Section): Section? {
        sectionRepository.findNextInTopic(currentSection.topicId, currentSection.order)
            ?.let { return it }

        val currentTopic = topicRepository.findById(currentSection.topicId) ?: return null
        val nextTopic = topicRepository.findNextInBlock(currentTopic.blockId, currentTopic.order)
            ?: return null

        return sectionRepository.findFirstInTopic(nextTopic.id)
    }
}
