package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest @Autowired constructor(
    private val studentRepository: StudentRepository,
) {

    @Test
    fun `findByTelegramChatId returns saved student`() {
        val student = Student(
            telegramChatId = 1L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )
        studentRepository.save(student)

        val found = studentRepository.findByTelegramChatId(1L)

        assertThat(found).isNotNull
        assertThat(found?.githubUsername).isEqualTo("octocat")
    }

    @Test
    fun `findByGithubUsername returns saved student`() {
        val student = Student(
            telegramChatId = 2L,
            githubUsername = "octocat2",
            githubName = "Octo Cat Two",
        )
        studentRepository.save(student)

        val found = studentRepository.findByGithubUsername("octocat2")

        assertThat(found).isNotNull
        assertThat(found?.telegramChatId).isEqualTo(2L)
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
