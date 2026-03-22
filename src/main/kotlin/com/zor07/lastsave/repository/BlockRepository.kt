package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.Block
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BlockRepository : JpaRepository<Block, Long> {
    @Query(
        value = """
            select * from block
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByOrderByOrderAsc(): Block?

    @Query(
        value = """
            select * from block
            where "order" > :orderVal
            order by "order" asc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByOrderGreaterThanOrderByOrderAsc(@Param("orderVal") order: Int): Block?
}
