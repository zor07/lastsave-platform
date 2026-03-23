package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.progress.BlockStartResult
import com.zor07.lastsave.service.progress.StudentProgressService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageCallbackServiceImpl(
    private val studentRepository: StudentRepository,
    private val messageRepository: MessageRepository,
    private val messageLogRepository: MessageLogRepository,
    private val studentProgressService: StudentProgressService,
) : MessageCallbackService {

    override fun handleCallback(chatId: Long, messageId: Long): BlockStartResult? {
        val student = studentRepository.findByTelegramChatId(chatId)
            ?: throw IllegalStateException("Student not found for chat $chatId")
        val message = messageRepository.findById(messageId).orElse(null)
            ?: throw IllegalStateException("Message $messageId not found")

        markCallbackReceived(requireNotNull(student.id) { "Student id is required" }, messageId)
        return studentProgressService.completeSectionAndAdvance(student, message.sectionId)
    }

    private fun markCallbackReceived(studentId: Long, messageId: Long) {
        val log = messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(studentId, messageId)
            ?: throw IllegalStateException("Message log not found for student $studentId and message $messageId")
        messageLogRepository.save(log.copy(callbackReceivedAt = LocalDateTime.now()))
    }
}
