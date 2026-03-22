package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : JpaRepository<Section, Long> {
    @Query(
        value = """
            select * from section
            where topic_id = :topicId
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByTopicIdOrderByOrderAsc(@Param("topicId") topicId: Long): Section?

    @Query(
        value = """
            select * from section
            where topic_id = :topicId
              and "order" > :orderVal
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(
        @Param("topicId") topicId: Long,
        @Param("orderVal") order: Int,
    ): Section?
}
