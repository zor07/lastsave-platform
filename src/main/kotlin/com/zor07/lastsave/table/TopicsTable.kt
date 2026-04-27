package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object TopicsTable : Table("topic") {
    val id = long("id").autoIncrement()
    val blockId = long("block_id")
    val title = varchar("title", 255)
    val order = integer("order")
    val code = varchar("code", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}
