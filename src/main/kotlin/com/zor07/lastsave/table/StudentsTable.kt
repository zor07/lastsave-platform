package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object StudentsTable : Table("students") {
    val id = long("id").autoIncrement()
    val telegramChatId = long("telegram_chat_id").uniqueIndex()
    val githubUsername = varchar("github_username", 255).uniqueIndex()
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}
