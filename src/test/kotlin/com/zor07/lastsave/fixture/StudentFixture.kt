package com.zor07.lastsave.fixture

import com.zor07.lastsave.model.Student
import java.time.LocalDateTime

object StudentFixture {
    fun student(
        id: Long = 1L,
        telegramChatId: Long = 100L,
        githubUsername: String = "user",
        createdAt: LocalDateTime = LocalDateTime.now(),
    ) = Student(
        id = id,
        telegramChatId = telegramChatId,
        githubUsername = githubUsername,
        createdAt = createdAt,
    )
}
