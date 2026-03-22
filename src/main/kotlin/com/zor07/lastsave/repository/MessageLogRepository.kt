package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.MessageLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MessageLogRepository : JpaRepository<MessageLog, Long> {
    @Query(
        value = """
            select * from message_log
            where student_id = :studentId
            order by sent_at desc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByStudentIdOrderBySentAtDesc(@Param("studentId") studentId: Long): MessageLog?

    @Query(
        value = """
            select * from message_log
            where student_id = :studentId
              and message_id = :messageId
            order by sent_at desc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByStudentIdAndMessageIdOrderBySentAtDesc(
        @Param("studentId") studentId: Long,
        @Param("messageId") messageId: Long,
    ): MessageLog?
}
