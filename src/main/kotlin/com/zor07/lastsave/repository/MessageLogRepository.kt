package com.zor07.lastsave.repository

import com.zor07.lastsave.model.MessageLog
import com.zor07.lastsave.model.NewMessageLog
import com.zor07.lastsave.table.MessageLogsTable
import com.zor07.lastsave.table.MessagesTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import org.springframework.stereotype.Repository

@Repository
class MessageLogRepository {

    fun save(newLog: NewMessageLog): MessageLog {
        val stmt = MessageLogsTable.insert {
            it[messageId] = newLog.messageId
            it[studentId] = newLog.studentId
            it[sentAt] = newLog.sentAt
        }
        return MessageLog(
            id = stmt[MessageLogsTable.id],
            messageId = newLog.messageId,
            studentId = newLog.studentId,
            sentAt = newLog.sentAt,
            callbackReceivedAt = null,
            prReceivedAt = null,
        )
    }

    fun findLastForStudentInSection(studentId: Long, sectionId: Long): MessageLog? =
        MessageLogsTable
            .join(MessagesTable, JoinType.INNER, MessageLogsTable.messageId, MessagesTable.id)
            .selectAll()
            .where { (MessageLogsTable.studentId eq studentId) and (MessagesTable.sectionId eq sectionId) }
            .orderBy(MessageLogsTable.sentAt to SortOrder.DESC)
            .limit(1)
            .singleOrNull()
            ?.toMessageLog()

    fun markCallbackReceived(id: Long) {
        MessageLogsTable.update({ MessageLogsTable.id eq id }) {
            it[callbackReceivedAt] = LocalDateTime.now()
        }
    }

    fun markPrReceived(id: Long) {
        MessageLogsTable.update({ MessageLogsTable.id eq id }) {
            it[prReceivedAt] = LocalDateTime.now()
        }
    }

    private fun ResultRow.toMessageLog() = MessageLog(
        id = this[MessageLogsTable.id],
        messageId = this[MessageLogsTable.messageId],
        studentId = this[MessageLogsTable.studentId],
        sentAt = this[MessageLogsTable.sentAt],
        callbackReceivedAt = this[MessageLogsTable.callbackReceivedAt],
        prReceivedAt = this[MessageLogsTable.prReceivedAt],
    )
}
