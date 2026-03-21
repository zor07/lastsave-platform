package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : JpaRepository<Topic, Long> {
    fun findFirstByBlockIdOrderByOrderAsc(blockId: Long): Topic?
    fun findFirstByBlockIdAndOrderGreaterThanOrderByOrderAsc(blockId: Long, order: Int): Topic?
}
