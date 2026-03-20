package com.zor07.lastsave.student

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@Testcontainers
@Import(StudentService::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentServiceTest @Autowired constructor(
    private val studentService: StudentService,
    private val studentRepository: StudentRepository,
) {

    @Test
    fun `registerStudent creates student when none exists`() {
        val student = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        assertThat(student.id).isNotNull()
        assertThat(studentRepository.count()).isEqualTo(1)
        assertThat(student.telegramChatId).isEqualTo(1L)
        assertThat(student.githubUsername).isEqualTo("octocat")
    }

    @Test
    fun `registerStudent returns existing by telegramChatId`() {
        val first = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        val second = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "someone-else",
            githubName = "Another User",
        )

        assertThat(second.id).isEqualTo(first.id)
        assertThat(studentRepository.count()).isEqualTo(1)
    }

    @Test
    fun `registerStudent returns existing by githubUsername`() {
        val first = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        val second = studentService.registerStudent(
            telegramChatId = 2L,
            githubUsername = "octocat",
            githubName = "Octo Cat Duplicate",
        )

        assertThat(second.id).isEqualTo(first.id)
        assertThat(studentRepository.count()).isEqualTo(1)
    }

    @Test
    fun `findByTelegramChatId returns student`() {
        val saved = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        val found = studentService.findByTelegramChatId(1L)

        assertThat(found?.id).isEqualTo(saved.id)
    }

    @Test
    fun `findByGithubUsername returns student`() {
        val saved = studentService.registerStudent(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        val found = studentService.findByGithubUsername("octocat")

        assertThat(found?.id).isEqualTo(saved.id)
    }

    companion object {
        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun dataSourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }
}
