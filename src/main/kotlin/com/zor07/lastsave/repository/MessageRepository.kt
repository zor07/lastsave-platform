package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    @Query(
        value = """
            select * from message
            where section_id = :sectionId
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstBySectionIdOrderByOrderAsc(@Param("sectionId") sectionId: Long): Message?

    @Query(
        value = """
            select * from message
            where section_id = :sectionId
              and "order" > :orderVal
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findNextMessageToSend(
        @Param("sectionId") sectionId: Long,
        @Param("orderVal") order: Int,
    ): Message?
}
