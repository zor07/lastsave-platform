package com.zor07.lastsave.student

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {

    @Transactional
    fun registerStudent(telegramChatId: Long, githubUsername: String, githubName: String): Student {
        studentRepository.findByTelegramChatId(telegramChatId)?.let { return it }
        studentRepository.findByGithubUsername(githubUsername)?.let { return it }

        val student = Student(
            telegramChatId = telegramChatId,
            githubUsername = githubUsername,
            githubName = githubName,
        )
        return studentRepository.save(student)
    }

    fun findByTelegramChatId(telegramChatId: Long): Student? =
        studentRepository.findByTelegramChatId(telegramChatId)

    fun findByGithubUsername(githubUsername: String): Student? =
        studentRepository.findByGithubUsername(githubUsername)
}
