package com.zor07.lastsave.repository

import com.zor07.lastsave.model.Topic
import com.zor07.lastsave.table.TopicsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class TopicRepository {

    fun findById(id: Long): Topic? =
        TopicsTable.selectAll()
            .where { TopicsTable.id eq id }
            .singleOrNull()
            ?.toTopic()

    fun findNextInBlock(blockId: Long, afterOrder: Int): Topic? =
        TopicsTable.selectAll()
            .where { (TopicsTable.blockId eq blockId) and (TopicsTable.order greater afterOrder) }
            .orderBy(TopicsTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toTopic()

    fun findFirstInBlock(blockId: Long): Topic? =
        TopicsTable.selectAll()
            .where { TopicsTable.blockId eq blockId }
            .orderBy(TopicsTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toTopic()

    private fun ResultRow.toTopic() = Topic(
        id = this[TopicsTable.id],
        blockId = this[TopicsTable.blockId],
        title = this[TopicsTable.title],
        order = this[TopicsTable.order],
    )
}
