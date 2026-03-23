package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.Message
import com.zor07.lastsave.entity.MessageLog
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.MessageSender
import com.zor07.lastsave.entity.enums.MessageWaitFor
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.progress.BlockStartResult
import com.zor07.lastsave.service.progress.StudentProgressService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class MessageCallbackServiceImplTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var messageRepository: MessageRepository

    @Mock
    private lateinit var messageLogRepository: MessageLogRepository

    @Mock
    private lateinit var studentProgressService: StudentProgressService

    @InjectMocks
    private lateinit var service: MessageCallbackServiceImpl

    @Test
    fun `handleCallback marks callback and starts next section`() {
        val student = Student(id = 1L, telegramChatId = 10L, githubUsername = "u", githubName = "n")
        val message = Message(
            id = 100L,
            sectionId = 200L,
            sender = MessageSender.BOT,
            text = "text",
            waitFor = MessageWaitFor.CALLBACK,
            order = 1,
        )
        val messageLog = MessageLog(
            id = 5L,
            messageId = 100L,
            studentId = 1L,
            sentAt = LocalDateTime.now().minusMinutes(1),
            callbackReceivedAt = null,
        )
        given(studentRepository.findByTelegramChatId(10L)).willReturn(student)
        given(messageRepository.findById(100L)).willReturn(Optional.of(message))
        given(messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(1L, 100L)).willReturn(messageLog)
        given(studentProgressService.completeSectionAndAdvance(student, 200L)).willReturn(null)

        val result = service.handleCallback(10L, 100L)

        val logCaptor = ArgumentCaptor.forClass(MessageLog::class.java)
        verify(messageLogRepository).save(logCaptor.capture())
        assertThat(logCaptor.value.callbackReceivedAt).isNotNull

        assertThat(result).isNull()
        verify(studentProgressService).completeSectionAndAdvance(student, 200L)
    }

    @Test
    fun `handleCallback starts next block when no next section`() {
        val student = Student(id = 1L, telegramChatId = 10L, githubUsername = "u", githubName = "n")
        val message = Message(
            id = 100L,
            sectionId = 200L,
            sender = MessageSender.BOT,
            text = "text",
            waitFor = MessageWaitFor.CALLBACK,
            order = 1,
        )
        val messageLog = MessageLog(
            id = 5L,
            messageId = 100L,
            studentId = 1L,
            sentAt = LocalDateTime.now().minusMinutes(1),
            callbackReceivedAt = null,
        )
        given(studentRepository.findByTelegramChatId(10L)).willReturn(student)
        given(messageRepository.findById(100L)).willReturn(Optional.of(message))
        given(messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(1L, 100L)).willReturn(messageLog)
        given(studentProgressService.completeSectionAndAdvance(student, 200L)).willReturn(
            BlockStartResult(
                progress = StudentProgress(
                    id = 99L,
                    studentId = 1L,
                    sectionId = 201L,
                    status = StudentProgressStatus.IN_PROGRESS,
                ),
                repoUrl = "repo",
                blockTitle = "block",
            ),
        )

        val result = service.handleCallback(10L, 100L)

        verify(studentProgressService).completeSectionAndAdvance(student, 200L)
        assertThat(result?.repoUrl).isEqualTo("repo")
    }
}
