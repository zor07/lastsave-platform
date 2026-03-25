package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object MessageLogsTable : Table("message_log") {
    val id = long("id").autoIncrement()
    val messageId = long("message_id")
    val studentId = long("student_id")
    val sentAt = datetime("sent_at")
    val callbackReceivedAt = datetime("callback_received_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
