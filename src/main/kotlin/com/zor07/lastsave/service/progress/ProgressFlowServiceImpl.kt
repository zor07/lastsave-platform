package com.zor07.lastsave.service.progress

import com.zor07.lastsave.enums.WaitFor
import com.zor07.lastsave.model.Message
import com.zor07.lastsave.model.NewMessageLog
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.service.material.MaterialService
import com.zor07.lastsave.service.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ProgressFlowServiceImpl(
    private val studentProgressRepository: StudentProgressRepository,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val notificationService: NotificationService,
    private val materialService: MaterialService,
    private val studentProgressService: StudentProgressService,
) : ProgressFlowService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun advanceIfPossible(student: Student) {
        val progress = studentProgressRepository.findActiveByStudentId(student.id) ?: run {
            logger.info("Student {} has no active progress, skipping", student.id)
            return
        }
        logger.info("Student {} is on section {}", student.id, progress.sectionId)

        val lastLog = messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)

        if (lastLog == null) {
            logger.info("Student {} has no messages sent in section {} yet, sending first", student.id, progress.sectionId)
            val first = messageRepository.findFirstInSection(progress.sectionId) ?: run {
                logger.info("Section {} has no messages, skipping", progress.sectionId)
                return
            }
            send(student, first)
            val materials = materialService.getSectionMaterials(progress.sectionId)
            if (materials.isNotEmpty()) {
                notificationService.sendText(student, materialService.formatMessage(materials))
            }
            return
        }

        val lastMessage = messageRepository.findById(lastLog.messageId) ?: return

        val ready = when (lastMessage.waitFor) {
            WaitFor.NOTHING  -> true
            WaitFor.CALLBACK -> lastLog.callbackReceivedAt != null
            WaitFor.PR       -> lastLog.prReceivedAt != null
        }

        if (!ready) {
            logger.info("Student {} waiting for {} on message {}", student.id, lastMessage.waitFor, lastMessage.id)
            return
        }

        val next = messageRepository.findNextInSection(progress.sectionId, lastMessage.order)
        if (next == null) {
            logger.info("Student {} finished section {}, advancing", student.id, progress.sectionId)
            studentProgressService.completeSectionAndAdvance(student, progress.sectionId)
            val newProgress = studentProgressRepository.findActiveByStudentId(student.id) ?: return
            val firstMessage = messageRepository.findFirstInSection(newProgress.sectionId) ?: return
            send(student, firstMessage)
            return
        }
        send(student, next)
    }

    private fun send(student: Student, message: Message) {
        notificationService.sendMessage(student, message)
        messageLogRepository.save(NewMessageLog(message.id, student.id, LocalDateTime.now()))
        logger.info("Sent message {} (waitFor={}) to student {}", message.id, message.waitFor, student.id)
    }
}
