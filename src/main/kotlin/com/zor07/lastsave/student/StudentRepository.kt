package com.zor07.lastsave.student

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    fun findByTelegramChatId(telegramChatId: Long): Student?
    fun findByGithubUsername(githubUsername: String): Student?
}
