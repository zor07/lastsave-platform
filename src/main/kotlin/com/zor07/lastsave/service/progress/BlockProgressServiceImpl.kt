package com.zor07.lastsave.service.progress

import com.zor07.lastsave.entity.Block
import com.zor07.lastsave.entity.Section
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.BlockRepository
import com.zor07.lastsave.repository.SectionRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.TopicRepository
import com.zor07.lastsave.service.github.GitHubService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BlockProgressServiceImpl(
    private val blockRepository: BlockRepository,
    private val topicRepository: TopicRepository,
    private val sectionRepository: SectionRepository,
    private val studentProgressRepository: StudentProgressRepository,
    private val gitHubService: GitHubService,
) : BlockProgressService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun startFirstBlockIfNeeded(student: Student): BlockStartResult? {
        val studentId = requireNotNull(student.id) { "Student id is required to start block" }
        val activeProgress = studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(
            studentId,
            StudentProgressStatus.IN_PROGRESS,
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

    private fun startBlock(student: Student, block: Block): BlockStartResult? {
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
}
