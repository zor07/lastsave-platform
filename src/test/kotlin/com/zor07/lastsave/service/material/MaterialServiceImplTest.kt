package com.zor07.lastsave.service.material

import com.zor07.lastsave.enums.MaterialType
import com.zor07.lastsave.fixture.MaterialFixture.material
import com.zor07.lastsave.repository.MaterialRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class MaterialServiceImplTest {

    private val materialRepository: MaterialRepository = mock()
    private val service = MaterialServiceImpl(materialRepository)

    @AfterEach
    fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(materialRepository)
    }

    // --- getSectionMaterials ---

    @Test
    fun `getSectionMaterials - returns materials from repository`() {
        val sectionId = 10L
        val materials = listOf(material(id = 1L), material(id = 2L))
        whenever(materialRepository.findBySectionId(sectionId)).thenReturn(materials)

        val result = service.getSectionMaterials(sectionId)

        assertThat(result).isEqualTo(materials)
        verify(materialRepository).findBySectionId(sectionId)
    }

    @Test
    fun `getSectionMaterials - returns empty list when no materials`() {
        val sectionId = 10L
        whenever(materialRepository.findBySectionId(sectionId)).thenReturn(emptyList())

        val result = service.getSectionMaterials(sectionId)

        assertThat(result).isEmpty()
        verify(materialRepository).findBySectionId(sectionId)
    }

    // --- formatMessage ---

    @Test
    fun `single THEORY material - correct format`() {
        val result = service.formatMessage(listOf(
            material(type = MaterialType.THEORY, title = "Справочник", url = "https://example.com/1")
        ))

        assertThat(result).isEqualTo("""
            Теория по текущему разделу:

            Справочник: https://example.com/1
        """.trimIndent())
    }

    @Test
    fun `single PRACTICE material - correct format`() {
        val result = service.formatMessage(listOf(
            material(type = MaterialType.PRACTICE, title = "Задание", url = "https://example.com/tasks")
        ))

        assertThat(result).isEqualTo("""
            Техническое задание:

            Задание: https://example.com/tasks
        """.trimIndent())
    }

    @Test
    fun `single VIDEO material - correct format`() {
        val result = service.formatMessage(listOf(
            material(type = MaterialType.VIDEO, title = "Лекция", url = "https://example.com/video")
        ))

        assertThat(result).isEqualTo("""
            Посмотри видео:

            Лекция: https://example.com/video
        """.trimIndent())
    }

    @Test
    fun `multiple THEORY materials - all listed under one header`() {
        val result = service.formatMessage(listOf(
            material(id = 1L, type = MaterialType.THEORY, title = "Справочник по синтаксису", url = "https://example.com/1"),
            material(id = 2L, type = MaterialType.THEORY, title = "Настройка окружения", url = "https://example.com/2"),
        ))

        assertThat(result).isEqualTo("""
            Теория по текущему разделу:

            Справочник по синтаксису: https://example.com/1
            Настройка окружения: https://example.com/2
        """.trimIndent())
    }

    @Test
    fun `mixed THEORY and PRACTICE materials - two sections`() {
        val result = service.formatMessage(listOf(
            material(id = 1L, type = MaterialType.THEORY, title = "Статья", url = "https://example.com/theory"),
            material(id = 2L, type = MaterialType.PRACTICE, title = "Задание", url = "https://example.com/tasks"),
        ))

        assertThat(result).isEqualTo("""
            Теория по текущему разделу:

            Статья: https://example.com/theory

            Техническое задание:

            Задание: https://example.com/tasks
        """.trimIndent())
    }

    @Test
    fun `mixed THEORY, PRACTICE and VIDEO materials - three sections`() {
        val result = service.formatMessage(listOf(
            material(id = 1L, type = MaterialType.THEORY, title = "Статья", url = "https://example.com/theory"),
            material(id = 2L, type = MaterialType.PRACTICE, title = "Задание", url = "https://example.com/tasks"),
            material(id = 3L, type = MaterialType.VIDEO, title = "Видео", url = "https://example.com/video"),
        ))

        assertThat(result).isEqualTo("""
            Теория по текущему разделу:

            Статья: https://example.com/theory

            Техническое задание:

            Задание: https://example.com/tasks

            Посмотри видео:

            Видео: https://example.com/video
        """.trimIndent())
    }
}
