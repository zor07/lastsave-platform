package com.zor07.lastsave.service.progress

import com.zor07.lastsave.enums.WaitFor
import com.zor07.lastsave.fixture.MaterialFixture.material
import com.zor07.lastsave.fixture.MessageFixture.message
import com.zor07.lastsave.fixture.MessageLogFixture.messageLog
import com.zor07.lastsave.fixture.StudentFixture.student
import com.zor07.lastsave.fixture.StudentProgressFixture.studentProgress
import com.zor07.lastsave.model.NewMessageLog
import com.zor07.lastsave.repository.MessageLogRepository
import com.zor07.lastsave.repository.MessageRepository
import com.zor07.lastsave.repository.StudentProgressRepository
import com.zor07.lastsave.service.material.MaterialService
import com.zor07.lastsave.service.notification.NotificationService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class ProgressFlowServiceImplTest {

    private val studentProgressRepository: StudentProgressRepository = mock()
    private val messageRepository: MessageRepository = mock()
    private val messageLogRepository: MessageLogRepository = mock()
    private val notificationService: NotificationService = mock()
    private val materialService: MaterialService = mock()
    private val studentProgressService: StudentProgressService = mock()

    private val service = ProgressFlowServiceImpl(
        studentProgressRepository = studentProgressRepository,
        messageRepository = messageRepository,
        messageLogRepository = messageLogRepository,
        notificationService = notificationService,
        materialService = materialService,
        studentProgressService = studentProgressService,
    )

    private val student = student()
    private val progress = studentProgress()
    private val firstMessage = message(id = 1L, order = 1)
    private val secondMessage = message(id = 2L, order = 2)

    @AfterEach
    fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(
            studentProgressRepository,
            messageRepository,
            messageLogRepository,
            notificationService,
            materialService,
            studentProgressService,
        )
    }

    // --- Нет активного прогресса ---

    @Test
    fun `no active progress - nothing is sent`() {
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(null)

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
    }

    // --- Первый тик в секции ---

    @Test
    fun `first tick in section, no materials - sends only first message`() {
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(null)
        whenever(messageRepository.findFirstInSection(progress.sectionId)).thenReturn(firstMessage)
        whenever(materialService.getSectionMaterials(progress.sectionId)).thenReturn(emptyList())
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findFirstInSection(progress.sectionId)
        verify(materialService).getSectionMaterials(progress.sectionId)
        verify(notificationService).sendMessage(student, firstMessage)
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `first tick in section, 1 material - sends first message and material`() {
        val material = material(id = 1L)
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(null)
        whenever(messageRepository.findFirstInSection(progress.sectionId)).thenReturn(firstMessage)
        whenever(materialService.getSectionMaterials(progress.sectionId)).thenReturn(listOf(material))
        whenever(materialService.formatMessage(listOf(material))).thenReturn("Материал 1")
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findFirstInSection(progress.sectionId)
        verify(materialService).getSectionMaterials(progress.sectionId)
        verify(materialService).formatMessage(listOf(material))
        verify(notificationService).sendMessage(student, firstMessage)
        verify(notificationService).sendText(student, "Материал 1")
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `first tick in section, multiple materials - sends first message and all materials`() {
        val materials = listOf(material(id = 1L), material(id = 2L))
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(null)
        whenever(messageRepository.findFirstInSection(progress.sectionId)).thenReturn(firstMessage)
        whenever(materialService.getSectionMaterials(progress.sectionId)).thenReturn(materials)
        whenever(materialService.formatMessage(materials)).thenReturn("Материал 1\nМатериал 2")
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findFirstInSection(progress.sectionId)
        verify(materialService).getSectionMaterials(progress.sectionId)
        verify(materialService).formatMessage(materials)
        verify(notificationService).sendMessage(student, firstMessage)
        verify(notificationService).sendText(student, "Материал 1\nМатериал 2")
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `first tick in section, section has no messages - nothing is sent`() {
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(null)
        whenever(messageRepository.findFirstInSection(progress.sectionId)).thenReturn(null)

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findFirstInSection(progress.sectionId)
    }

    // --- Ожидание ---

    @Test
    fun `last message NOTHING - sends next message`() {
        val log = messageLog(messageId = firstMessage.id)
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.NOTHING))
        whenever(messageRepository.findNextInSection(progress.sectionId, firstMessage.order)).thenReturn(secondMessage)
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
        verify(messageRepository).findNextInSection(progress.sectionId, firstMessage.order)
        verify(notificationService).sendMessage(student, secondMessage)
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `last message CALLBACK, not clicked - nothing is sent`() {
        val log = messageLog(messageId = firstMessage.id, callbackReceivedAt = null)
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.CALLBACK))

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
    }

    @Test
    fun `last message CALLBACK, clicked - sends next message`() {
        val log = messageLog(messageId = firstMessage.id, callbackReceivedAt = LocalDateTime.now())
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.CALLBACK))
        whenever(messageRepository.findNextInSection(progress.sectionId, firstMessage.order)).thenReturn(secondMessage)
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
        verify(messageRepository).findNextInSection(progress.sectionId, firstMessage.order)
        verify(notificationService).sendMessage(student, secondMessage)
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `last message PR, not received - nothing is sent`() {
        val log = messageLog(messageId = firstMessage.id, prReceivedAt = null)
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.PR))

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
    }

    @Test
    fun `last message PR, received - sends next message`() {
        val log = messageLog(messageId = firstMessage.id, prReceivedAt = LocalDateTime.now())
        whenever(studentProgressRepository.findActiveByStudentId(student.id)).thenReturn(progress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.PR))
        whenever(messageRepository.findNextInSection(progress.sectionId, firstMessage.order)).thenReturn(secondMessage)
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
        verify(messageRepository).findNextInSection(progress.sectionId, firstMessage.order)
        verify(notificationService).sendMessage(student, secondMessage)
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    // --- Переход между секциями ---

    @Test
    fun `all messages sent, next section exists - advances and sends first message with materials`() {
        val log = messageLog(messageId = firstMessage.id)
        val newProgress = studentProgress(sectionId = 20L)
        val newSectionFirstMessage = message(id = 3L, sectionId = 20L, order = 1)
        val material = material(id = 1L, sectionId = 20L)
        whenever(studentProgressRepository.findActiveByStudentId(student.id))
            .thenReturn(progress)
            .thenReturn(newProgress)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.NOTHING))
        whenever(messageRepository.findNextInSection(progress.sectionId, firstMessage.order)).thenReturn(null)
        whenever(messageRepository.findFirstInSection(newProgress.sectionId)).thenReturn(newSectionFirstMessage)
        whenever(materialService.getSectionMaterials(newProgress.sectionId)).thenReturn(listOf(material))
        whenever(materialService.formatMessage(listOf(material))).thenReturn("Материал новой секции")
        whenever(messageLogRepository.save(any<NewMessageLog>())).thenReturn(messageLog())

        service.process(student)

        verify(studentProgressRepository, times(2)).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
        verify(messageRepository).findNextInSection(progress.sectionId, firstMessage.order)
        verify(studentProgressService).completeSectionAndAdvance(student, progress.sectionId)
        verify(messageRepository).findFirstInSection(newProgress.sectionId)
        verify(materialService).getSectionMaterials(newProgress.sectionId)
        verify(materialService).formatMessage(listOf(material))
        verify(notificationService).sendMessage(student, newSectionFirstMessage)
        verify(notificationService).sendText(student, "Материал новой секции")
        verify(messageLogRepository).save(any<NewMessageLog>())
    }

    @Test
    fun `all messages sent, no next section - advances and sends nothing`() {
        val log = messageLog(messageId = firstMessage.id)
        whenever(studentProgressRepository.findActiveByStudentId(student.id))
            .thenReturn(progress)
            .thenReturn(null)
        whenever(messageLogRepository.findLastForStudentInSection(student.id, progress.sectionId)).thenReturn(log)
        whenever(messageRepository.findById(firstMessage.id)).thenReturn(firstMessage.copy(waitFor = WaitFor.NOTHING))
        whenever(messageRepository.findNextInSection(progress.sectionId, firstMessage.order)).thenReturn(null)

        service.process(student)

        verify(studentProgressRepository, times(2)).findActiveByStudentId(student.id)
        verify(messageLogRepository).findLastForStudentInSection(student.id, progress.sectionId)
        verify(messageRepository).findById(firstMessage.id)
        verify(messageRepository).findNextInSection(progress.sectionId, firstMessage.order)
        verify(studentProgressService).completeSectionAndAdvance(student, progress.sectionId)
    }
}
