package com.zor07.lastsave.model

import java.time.LocalDateTime

data class NewStudent(
    val telegramChatId: Long,
    val githubUsername: String,
)

data class Student(
    val id: Long,
    val telegramChatId: Long,
    val githubUsername: String,
    val createdAt: LocalDateTime,
)
