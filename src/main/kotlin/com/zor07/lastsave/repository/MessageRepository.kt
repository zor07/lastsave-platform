package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findFirstBySectionIdOrderByOrderAsc(sectionId: Long): Message?
    fun findFirstBySectionIdAndOrderGreaterThanOrderByOrderAsc(sectionId: Long, order: Int): Message?
}
