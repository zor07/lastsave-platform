package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Block
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlockRepository : JpaRepository<Block, Long> {
    fun findFirstByOrderByOrderAsc(): Block?
    fun findFirstByOrderGreaterThanOrderByOrderAsc(order: Int): Block?
}
