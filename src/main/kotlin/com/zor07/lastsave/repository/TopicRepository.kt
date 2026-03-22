package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : JpaRepository<Topic, Long> {
    @Query(
        value = """
            select * from topic
            where block_id = :blockId
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByBlockIdOrderByOrderAsc(@Param("blockId") blockId: Long): Topic?

    @Query(
        value = """
            select * from topic
            where block_id = :blockId
              and "order" > :orderVal
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByBlockIdAndOrderGreaterThanOrderByOrderAsc(
        @Param("blockId") blockId: Long,
        @Param("orderVal") order: Int,
    ): Topic?
}
