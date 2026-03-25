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
            where telegram_chat_id = :chatId
            limit 1
        """,
        nativeQuery = true,
    )
    fun findByChatId(@Param("chatId") chatId: Long): Student?
}
