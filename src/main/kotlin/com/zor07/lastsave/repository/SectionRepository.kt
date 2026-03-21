package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Section
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : JpaRepository<Section, Long> {
    fun findFirstByTopicIdOrderByOrderAsc(topicId: Long): Section?
    fun findFirstByTopicIdAndOrderGreaterThanOrderByOrderAsc(topicId: Long, order: Int): Section?
}
