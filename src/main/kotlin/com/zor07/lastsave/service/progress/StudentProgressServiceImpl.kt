package com.zor07.lastsave.service.progress

import com.zor07.lastsave.entity.Block
import com.zor07.lastsave.entity.Section
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.event.BlockStartedEvent
import com.zor07.lastsave.repository.BlockRepository
import com.zor07.lastsave.repository.SectionRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.TopicRepository
import com.zor07.lastsave.service.github.GitHubService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class StudentProgressServiceImpl(
    private val blockRepository: BlockRepository,
    private val topicRepository: TopicRepository,
    private val sectionRepository: SectionRepository,
    private val studentProgressRepository: StudentProgressRepository,
    private val gitHubService: GitHubService,
    private val eventPublisher: ApplicationEventPublisher,
) : StudentProgressService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getOrStartProgress(student: Student): StudentProgress {
        val studentId = requireNotNull(student.id) { "Student id is required" }
        val activeProgress = studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(
            studentId,
            StudentProgressStatus.IN_PROGRESS.name,
        )
        if (activeProgress != null) {
            return activeProgress
        }
        return startFirstBlockIfNeeded(student)?.progress!!
    }

    override fun startFirstBlockIfNeeded(student: Student): BlockStartResult? {
        val studentId = requireNotNull(student.id) { "Student id is required to start block" }
        val activeProgress = studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(
            studentId,
            StudentProgressStatus.IN_PROGRESS.name,
        )
        if (activeProgress != null) {
            return null
        }
        val block = blockRepository.findFirstByOrderByOrderAsc()
            ?: throw IllegalStateException("No blocks available to start")
        return startBlock(student, block)
    }

    override fun startNextBlockIfExists(student: Student, currentSection: Section): BlockStartResult? {
        val topic = topicRepository.findById(currentSection.topicId)
            .orElseThrow { IllegalStateException("Topic ${currentSection.topicId} not found") }
        val currentBlock = blockRepository.findById(topic.blockId)
            .orElseThrow { IllegalStateException("Block ${topic.blockId} not found") }
        val nextBlock = blockRepository.findFirstByOrderGreaterThanOrderByOrderAsc(currentBlock.order)
        return if (nextBlock != null) startBlock(student, nextBlock) else null
    }

    override fun completeSectionAndAdvance(student: Student, currentSectionId: Long): BlockStartResult? {
        val studentId = requireNotNull(student.id) { "Student id is required" }
        markSectionCompleted(studentId, currentSectionId)

        val currentSection = sectionRepository.findById(currentSectionId)
            .orElseThrow { IllegalStateException("Section $currentSectionId not found") }
        val nextSection = findNextSection(currentSection)
        if (nextSection != null) {
            val progress = StudentProgress(
                studentId = studentId,
                sectionId = requireNotNull(nextSection.id) { "Next section id is required" },
                status = StudentProgressStatus.IN_PROGRESS,
                startedAt = LocalDateTime.now(),
            )
            studentProgressRepository.save(progress)
            return null
        }

        return startNextBlockIfExists(student, currentSection)
    }

    private fun markSectionCompleted(studentId: Long, sectionId: Long) {
        val progress = studentProgressRepository.findByStudentIdAndSectionId(studentId, sectionId)
            ?: throw IllegalStateException("Student progress not found for student $studentId section $sectionId")
        val updated = progress.copy(
            status = StudentProgressStatus.COMPLETED,
            completedAt = LocalDateTime.now(),
        )
        studentProgressRepository.save(updated)
    }

    private fun startBlock(student: Student, block: Block): BlockStartResult {
        val blockId = requireNotNull(block.id) { "Block id is required" }
        val firstSection = getFirstSection(blockId)
        val repoUrl = gitHubService.createRepoFromTemplate(block.templateRepoName, student.githubUsername)

        val progress = StudentProgress(
            studentId = requireNotNull(student.id) { "Student id is required" },
            sectionId = requireNotNull(firstSection.id) { "Section id is required" },
            status = StudentProgressStatus.IN_PROGRESS,
            startedAt = LocalDateTime.now(),
        )
        val saved = studentProgressRepository.save(progress)
        eventPublisher.publishEvent(BlockStartedEvent(student, repoUrl, block.title))
        logger.info("Started block {} for student {}", blockId, student.id)
        return BlockStartResult(progress = saved, repoUrl = repoUrl, blockTitle = block.title)
    }

    private fun getFirstSection(blockId: Long): Section {
        val topic = topicRepository.findFirstByBlockIdOrderByOrderAsc(blockId)
            ?: throw IllegalStateException("No topic for block $blockId")
        val topicId = requireNotNull(topic.id) { "Topic id is required" }
        return sectionRepository.findFirstByTopicIdOrderByOrderAsc(topicId)
            ?: throw IllegalStateException("No section for topic $topicId")
    }

    private fun findNextSection(currentSection: Section): Section? {
        val nextInTopic = sectionRepository.findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(
            currentSection.topicId,
            currentSection.order,
        )
        if (nextInTopic != null) {
            return nextInTopic
        }
        val topic = topicRepository.findById(currentSection.topicId)
            .orElseThrow { IllegalStateException("Topic ${currentSection.topicId} not found") }
        val nextTopic = topicRepository.findFirstByBlockIdAndOrderGreaterThanOrderByOrderAsc(topic.blockId, topic.order)
            ?: return null
        val nextTopicId = requireNotNull(nextTopic.id) { "Next topic id is required" }
        return sectionRepository.findFirstByTopicIdOrderByOrderAsc(nextTopicId)
            ?: throw IllegalStateException("No sections for topic $nextTopicId")
    }
}
