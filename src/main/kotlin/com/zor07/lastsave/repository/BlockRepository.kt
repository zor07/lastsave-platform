package com.zor07.lastsave.repository

import com.zor07.lastsave.model.Block
import com.zor07.lastsave.table.BlocksTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class BlockRepository {

    fun findById(id: Long): Block? =
        BlocksTable.selectAll()
            .where { BlocksTable.id eq id }
            .singleOrNull()
            ?.toBlock()

    fun findNextByOrder(afterOrder: Int): Block? =
        BlocksTable.selectAll()
            .where { BlocksTable.order greater afterOrder }
            .orderBy(BlocksTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toBlock()

    fun findFirst(): Block? =
        BlocksTable.selectAll()
            .orderBy(BlocksTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toBlock()

    private fun ResultRow.toBlock() = Block(
        id = this[BlocksTable.id],
        courseId = this[BlocksTable.courseId],
        title = this[BlocksTable.title],
        gitRepositoryId = this[BlocksTable.gitRepositoryId],
        order = this[BlocksTable.order],
    )
}
