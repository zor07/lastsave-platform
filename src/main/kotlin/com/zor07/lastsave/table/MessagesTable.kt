package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object MessagesTable : Table("message") {
    val id = long("id").autoIncrement()
    val sectionId = long("section_id")
    val sender = varchar("sender", 50)
    val text = text("text")
    val callbackText = varchar("callback_text", 255).nullable()
    val waitFor = varchar("wait_for", 50)
    val order = integer("order")

    override val primaryKey = PrimaryKey(id)
}
