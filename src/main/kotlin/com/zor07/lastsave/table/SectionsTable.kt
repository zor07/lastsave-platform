package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object SectionsTable : Table("section") {
    val id = long("id").autoIncrement()
    val topicId = long("topic_id")
    val title = varchar("title", 255)
    val order = integer("order")
    val unlockCondition = varchar("unlock_condition", 50)

    override val primaryKey = PrimaryKey(id)
}
