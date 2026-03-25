package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object MaterialsTable : Table("material") {
    val id = long("id").autoIncrement()
    val sectionId = long("section_id")
    val type = varchar("type", 50)
    val title = varchar("title", 255)
    val url = varchar("url", 1024)
    val order = integer("order")

    override val primaryKey = PrimaryKey(id)
}
