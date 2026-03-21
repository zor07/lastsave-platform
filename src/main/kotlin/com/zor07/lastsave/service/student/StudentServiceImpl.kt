package com.zor07.lastsave.service.student

import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentServiceImpl(
    private val studentRepository: StudentRepository,
) : StudentService {

    @Transactional
    override fun registerStudent(telegramChatId: Long, githubUsername: String, githubName: String): Student {
        studentRepository.findByTelegramChatId(telegramChatId)?.let { return it }
        studentRepository.findByGithubUsername(githubUsername)?.let { return it }

        val student = Student(
            telegramChatId = telegramChatId,
            githubUsername = githubUsername,
            githubName = githubName,
        )
        return studentRepository.save(student)
    }

    override fun findByTelegramChatId(telegramChatId: Long): Student? =
        studentRepository.findByTelegramChatId(telegramChatId)

    override fun findByGithubUsername(githubUsername: String): Student? =
        studentRepository.findByGithubUsername(githubUsername)
}
