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
import java.util.Locale

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
        val activeProgress = studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(
            student.id ?: return null,
            StudentProgressStatus.IN_PROGRESS,
        )
        if (activeProgress != null) {
            return null
        }
        val block = blockRepository.findFirstByOrderByOrderAsc() ?: return null
        return startBlock(student, block)
    }

    override fun startNextBlockIfExists(student: Student, currentSection: Section): BlockStartResult? {
        val topic = topicRepository.findById(currentSection.topicId).orElse(null) ?: return null
        val currentBlock = blockRepository.findById(topic.blockId).orElse(null) ?: return null
        val nextBlock = blockRepository.findFirstByOrderGreaterThanOrderByOrderAsc(currentBlock.order) ?: return null
        return startBlock(student, nextBlock)
    }

    private fun startBlock(student: Student, block: Block): BlockStartResult? {
        val blockId = block.id ?: return null
        val firstSection = getFirstSection(blockId) ?: return null
        val repoUrl = gitHubService.createRepoFromTemplate(block.templateRepoName, student.githubUsername)

        val progress = StudentProgress(
            studentId = student.id ?: return null,
            sectionId = firstSection.id ?: return null,
            status = StudentProgressStatus.IN_PROGRESS,
            startedAt = LocalDateTime.now(),
        )
        val saved = studentProgressRepository.save(progress)
        logger.info("Started block {} for student {}", blockId, student.id)
        return BlockStartResult(progress = saved, repoUrl = repoUrl, blockTitle = block.title)
    }

    private fun getFirstSection(blockId: Long): Section? {
        val topic = topicRepository.findFirstByBlockIdOrderByOrderAsc(blockId) ?: return null
        return sectionRepository.findFirstByTopicIdOrderByOrderAsc(topic.id ?: return null)
    }

    private fun slug(title: String): String {
        val lowered = title.lowercase(Locale.getDefault())
        val dashed = lowered.replace("\\s+".toRegex(), "-")
        val sanitized = dashed.replace("[^a-z0-9-]".toRegex(), "")
        return sanitized.ifBlank { "course" }
    }
}
