package com.zor07.lastsave.service.student

import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StudentServiceImplTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentServiceImpl

    @Test
    fun `registerStudent returns existing by telegram chat id`() {
        val existing = Student(
            id = 1L,
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        given(studentRepository.findByTelegramChatId(1L)).willReturn(existing)

        val result = studentService.registerStudent(1L, "another", "Another")

        assertThat(result).isEqualTo(existing)
        verify(studentRepository, never()).findByGithubUsername(anyString())
        verify(studentRepository, never()).save(any())
    }

    @Test
    fun `registerStudent returns existing by github username`() {
        val existing = Student(
            id = 1L,
            telegramChatId = 2L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        given(studentRepository.findByTelegramChatId(1L)).willReturn(null)
        given(studentRepository.findByGithubUsername("octocat")).willReturn(existing)

        val result = studentService.registerStudent(1L, "octocat", "Name")

        assertThat(result).isEqualTo(existing)
        verify(studentRepository, never()).save(any())
    }

    @Test
    fun `registerStudent saves new student when not exists`() {
        given(studentRepository.findByTelegramChatId(1L)).willReturn(null)
        given(studentRepository.findByGithubUsername("octocat")).willReturn(null)
        val saved = Student(
            id = 10L,
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        given(studentRepository.save(any())).willReturn(saved)

        val result = studentService.registerStudent(1L, "octocat", "Octo Cat")

        val captor = ArgumentCaptor.forClass(Student::class.java)
        verify(studentRepository).save(captor.capture())
        assertThat(captor.value.telegramChatId).isEqualTo(1L)
        assertThat(captor.value.githubUsername).isEqualTo("octocat")
        assertThat(result).isEqualTo(saved)
    }

    @Test
    fun `findByTelegramChatId delegates to repository`() {
        val student = Student(
            id = 1L,
            telegramChatId = 5L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        given(studentRepository.findByTelegramChatId(5L)).willReturn(student)

        val result = studentService.findByTelegramChatId(5L)

        assertThat(result).isEqualTo(student)
    }

    @Test
    fun `findByGithubUsername delegates to repository`() {
        val student = Student(
            id = 1L,
            telegramChatId = 5L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        given(studentRepository.findByGithubUsername("octocat")).willReturn(student)

        val result = studentService.findByGithubUsername("octocat")

        assertThat(result).isEqualTo(student)
    }
}
