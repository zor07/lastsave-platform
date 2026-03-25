package com.zor07.lastsave.service.student

import com.zor07.lastsave.model.Student

interface StudentService {
    fun registerStudent(telegramChatId: Long, githubUsername: String): Student
    fun findByChatId(telegramChatId: Long): Student?
    fun findByGithubUsername(githubUsername: String): Student?
}
