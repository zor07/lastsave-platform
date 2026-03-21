package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.SectionRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.repository.TopicRepository
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.progress.BlockProgressService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageCallbackServiceImpl(
    private val studentRepository: StudentRepository,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val studentProgressRepository: StudentProgressRepository,
    private val sectionRepository: SectionRepository,
    private val topicRepository: TopicRepository,
    private val blockProgressService: BlockProgressService,
    private val telegramBot: TelegramBot,
) : MessageCallbackService {

    override fun handleCallback(chatId: Long, messageId: Long, callbackQueryId: String) {
        val student = studentRepository.findByTelegramChatId(chatId) ?: return
        val message = messageRepository.findById(messageId).orElse(null) ?: return

        markCallbackReceived(student.id ?: return, messageId)
        completeCurrentSection(student.id, message.sectionId)
        progressToNextSectionOrBlock(student.id, message.sectionId)

        telegramBot.answerCallback(callbackQueryId)
    }

    private fun markCallbackReceived(studentId: Long, messageId: Long) {
        val log = messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(studentId, messageId)
            ?: return
        val updated = log.copy(callbackReceivedAt = LocalDateTime.now())
        messageLogRepository.save(updated)
    }

    private fun completeCurrentSection(studentId: Long, sectionId: Long) {
        val progress = studentProgressRepository.findByStudentIdAndSectionId(studentId, sectionId) ?: return
        val updated = progress.copy(status = StudentProgressStatus.COMPLETED, completedAt = LocalDateTime.now())
        studentProgressRepository.save(updated)
    }

    private fun progressToNextSectionOrBlock(studentId: Long, currentSectionId: Long) {
        val currentSection = sectionRepository.findById(currentSectionId).orElse(null) ?: return
        val nextSection = findNextSection(currentSection)
        if (nextSection != null) {
            val progress = StudentProgress(
                studentId = studentId,
                sectionId = nextSection.id ?: return,
                status = StudentProgressStatus.IN_PROGRESS,
                startedAt = LocalDateTime.now(),
            )
            studentProgressRepository.save(progress)
        } else {
            val student = studentRepository.findById(studentId).orElse(null) ?: return
            blockProgressService.startNextBlockIfExists(student, currentSection)
        }
    }

    private fun findNextSection(currentSection: com.zor07.lastsave.entity.Section): com.zor07.lastsave.entity.Section? {
        val nextInTopic = sectionRepository.findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(
            currentSection.topicId,
            currentSection.order,
        )
        if (nextInTopic != null) {
            return nextInTopic
        }
        val topic = topicRepository.findById(currentSection.topicId).orElse(null) ?: return null
        val nextTopic = topicRepository.findFirstByBlockIdAndOrderGreaterThanOrderByOrderAsc(topic.blockId, topic.order)
            ?: return null
        return sectionRepository.findFirstByTopicIdOrderByOrderAsc(nextTopic.id ?: return null)
    }
}
