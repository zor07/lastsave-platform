package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.Message
import com.zor07.lastsave.entity.MessageLog
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.MessageWaitFor
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.bot.TelegramBot
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class MessageSchedulerServiceImplTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var messageRepository: MessageRepository

    @Mock
    private lateinit var messageLogRepository: MessageLogRepository

    @Mock
    private lateinit var studentProgressRepository: StudentProgressRepository

    @Mock
    private lateinit var telegramBot: TelegramBot

    @InjectMocks
    private lateinit var service: MessageSchedulerServiceImpl

    @Test
    fun `sends first message when no log`() {
        val student = Student(id = 1L, telegramChatId = 100L, githubUsername = "u", githubName = "n")
        val progress = StudentProgress(
            id = 10L,
            studentId = 1L,
            sectionId = 5L,
            status = StudentProgressStatus.IN_PROGRESS,
        )
        val message = Message(
            id = 20L,
            sectionId = 5L,
            sender = com.zor07.lastsave.entity.enums.MessageSender.BOT,
            text = "hello",
            waitFor = MessageWaitFor.NOTHING,
            order = 1,
        )

        given(studentRepository.findAll()).willReturn(listOf(student))
        given(studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(1L, StudentProgressStatus.IN_PROGRESS))
            .willReturn(progress)
        given(messageLogRepository.findFirstByStudentIdOrderBySentAtDesc(1L)).willReturn(null)
        given(messageRepository.findFirstBySectionIdOrderByOrderAsc(5L)).willReturn(message)

        service.scheduleMessages()

        verify(telegramBot).sendTextMessage(100L, "hello")
        verify(messageLogRepository).save(any(MessageLog::class.java))
    }

    @Test
    fun `does not send next message when callback not received`() {
        val student = Student(id = 1L, telegramChatId = 100L, githubUsername = "u", githubName = "n")
        val progress = StudentProgress(
            id = 10L,
            studentId = 1L,
            sectionId = 5L,
            status = StudentProgressStatus.IN_PROGRESS,
        )
        val message = Message(
            id = 20L,
            sectionId = 5L,
            sender = com.zor07.lastsave.entity.enums.MessageSender.BOT,
            text = "hello",
            waitFor = MessageWaitFor.CALLBACK,
            order = 1,
        )
        val log = MessageLog(
            id = 30L,
            messageId = 20L,
            studentId = 1L,
            sentAt = LocalDateTime.now(),
            callbackReceivedAt = null,
        )

        given(studentRepository.findAll()).willReturn(listOf(student))
        given(studentProgressRepository.findFirstByStudentIdAndStatusOrderByStartedAtDesc(1L, StudentProgressStatus.IN_PROGRESS))
            .willReturn(progress)
        given(messageLogRepository.findFirstByStudentIdOrderBySentAtDesc(1L)).willReturn(log)
        given(messageRepository.findById(20L)).willReturn(Optional.of(message))

        service.scheduleMessages()

        verify(telegramBot, never()).sendTextMessage(anyLong(), anyString())
        verify(telegramBot, never()).sendMessageWithButton(anyLong(), anyString(), anyString(), anyString())
    }
}
