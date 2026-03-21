package com.zor07.lastsave.service.student

import com.zor07.lastsave.entity.Student

interface StudentService {
    fun registerStudent(telegramChatId: Long, githubUsername: String, githubName: String): Student
    fun findByTelegramChatId(telegramChatId: Long): Student?
    fun findByGithubUsername(githubUsername: String): Student?
}
