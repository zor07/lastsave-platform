package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.MessageSender
import com.zor07.lastsave.enums.WaitFor
import com.zor07.lastsave.model.Message
import com.zor07.lastsave.table.MessagesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class MessageRepository {

    fun findById(id: Long): Message? =
        MessagesTable.selectAll()
            .where { MessagesTable.id eq id }
            .singleOrNull()
            ?.toMessage()

    fun findFirstInSection(sectionId: Long): Message? =
        MessagesTable.selectAll()
            .where { MessagesTable.sectionId eq sectionId }
            .orderBy(MessagesTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toMessage()

    fun findNextInSection(sectionId: Long, afterOrder: Int): Message? =
        MessagesTable.selectAll()
            .where { (MessagesTable.sectionId eq sectionId) and (MessagesTable.order greater afterOrder) }
            .orderBy(MessagesTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toMessage()

    private fun ResultRow.toMessage() = Message(
        id = this[MessagesTable.id],
        sectionId = this[MessagesTable.sectionId],
        sender = MessageSender.valueOf(this[MessagesTable.sender]),
        text = this[MessagesTable.text],
        callbackText = this[MessagesTable.callbackText],
        waitFor = WaitFor.valueOf(this[MessagesTable.waitFor]),
        order = this[MessagesTable.order],
    )
}
