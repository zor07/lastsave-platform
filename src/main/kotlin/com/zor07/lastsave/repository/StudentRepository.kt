package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    @Query(
        value = """
            select * from students
            where telegram_chat_id = :telegramChatId
            limit 1
        """,
        nativeQuery = true,
    )
    fun findByTelegramChatId(@Param("telegramChatId") telegramChatId: Long): Student?

    @Query(
        value = """
            select * from students
            where github_username = :githubUsername
            limit 1
        """,
        nativeQuery = true,
    )
    fun findByGithubUsername(@Param("githubUsername") githubUsername: String): Student?
}
