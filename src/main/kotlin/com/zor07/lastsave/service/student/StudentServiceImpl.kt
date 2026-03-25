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
    override fun registerStudent(telegramChatId: Long, githubUsername: String): Student {
        studentRepository.findByChatId(telegramChatId)?.let { return it }

        val student = Student(
            telegramChatId = telegramChatId,
            githubUsername = githubUsername,
        )
        return studentRepository.save(student)
    }

    override fun findByChatId(telegramChatId: Long): Student? =
        studentRepository.findByChatId(telegramChatId)
}
