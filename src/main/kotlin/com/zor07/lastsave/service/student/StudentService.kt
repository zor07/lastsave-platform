package com.zor07.lastsave.service.student

import com.zor07.lastsave.entity.Student

interface StudentService {
    fun registerStudent(telegramChatId: Long, githubUsername: String): Student
    fun findByChatId(telegramChatId: Long): Student?
}
