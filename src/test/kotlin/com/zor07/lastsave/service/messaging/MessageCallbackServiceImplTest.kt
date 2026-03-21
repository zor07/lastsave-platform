package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.entity.Message
import com.zor07.lastsave.entity.MessageLog
import com.zor07.lastsave.entity.Section
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.Topic
import com.zor07.lastsave.entity.enums.MessageSender
import com.zor07.lastsave.entity.enums.MessageWaitFor
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.SectionRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.repository.TopicRepository
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.progress.BlockProgressService
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
    private lateinit var studentProgressRepository: StudentProgressRepository

    @Mock
    private lateinit var sectionRepository: SectionRepository

    @Mock
    private lateinit var topicRepository: TopicRepository

    @Mock
    private lateinit var blockProgressService: BlockProgressService

    @Mock
    private lateinit var telegramBot: TelegramBot

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
        val progress = StudentProgress(
            id = 50L,
            studentId = 1L,
            sectionId = 200L,
            status = StudentProgressStatus.IN_PROGRESS,
        )
        val currentSection = Section(id = 200L, topicId = 300L, title = "s", order = 1, unlockCondition = com.zor07.lastsave.entity.enums.SectionUnlockCondition.MANUAL)
        val nextSection = Section(id = 201L, topicId = 300L, title = "s2", order = 2, unlockCondition = com.zor07.lastsave.entity.enums.SectionUnlockCondition.MANUAL)

        given(studentRepository.findByTelegramChatId(10L)).willReturn(student)
        given(messageRepository.findById(100L)).willReturn(Optional.of(message))
        given(messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(1L, 100L)).willReturn(messageLog)
        given(studentProgressRepository.findByStudentIdAndSectionId(1L, 200L)).willReturn(progress)
        given(sectionRepository.findById(200L)).willReturn(Optional.of(currentSection))
        given(sectionRepository.findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(300L, 1)).willReturn(nextSection)

        service.handleCallback(10L, 100L, "cb-id")

        val logCaptor = ArgumentCaptor.forClass(MessageLog::class.java)
        verify(messageLogRepository).save(logCaptor.capture())
        assertThat(logCaptor.value.callbackReceivedAt).isNotNull

        val progressCaptor = ArgumentCaptor.forClass(StudentProgress::class.java)
        verify(studentProgressRepository, org.mockito.Mockito.times(2)).save(progressCaptor.capture())
        val savedProgresses = progressCaptor.allValues
        assertThat(savedProgresses.any { it.status == StudentProgressStatus.COMPLETED }).isTrue()
        assertThat(savedProgresses.any { it.sectionId == 201L && it.status == StudentProgressStatus.IN_PROGRESS }).isTrue()

        verify(telegramBot).answerCallback("cb-id")
        verifyNoInteractions(blockProgressService)
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
        val progress = StudentProgress(
            id = 50L,
            studentId = 1L,
            sectionId = 200L,
            status = StudentProgressStatus.IN_PROGRESS,
        )
        val currentSection = Section(id = 200L, topicId = 300L, title = "s", order = 2, unlockCondition = com.zor07.lastsave.entity.enums.SectionUnlockCondition.MANUAL)
        val topic = Topic(id = 300L, blockId = 400L, title = "t", order = 2)

        given(studentRepository.findByTelegramChatId(10L)).willReturn(student)
        given(messageRepository.findById(100L)).willReturn(Optional.of(message))
        given(messageLogRepository.findFirstByStudentIdAndMessageIdOrderBySentAtDesc(1L, 100L)).willReturn(messageLog)
        given(studentProgressRepository.findByStudentIdAndSectionId(1L, 200L)).willReturn(progress)
        given(sectionRepository.findById(200L)).willReturn(Optional.of(currentSection))
        given(topicRepository.findById(300L)).willReturn(Optional.of(topic))
        given(sectionRepository.findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(300L, 2)).willReturn(null)
        given(topicRepository.findFirstByBlockIdAndOrderGreaterThanOrderByOrderAsc(400L, 2)).willReturn(null)
        given(studentRepository.findById(1L)).willReturn(Optional.of(student))

        service.handleCallback(10L, 100L, "cb-id")

        verify(blockProgressService).startNextBlockIfExists(student, currentSection)
        verify(telegramBot).answerCallback("cb-id")
    }
}
