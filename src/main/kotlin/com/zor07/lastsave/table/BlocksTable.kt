package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object BlocksTable : Table("block") {
    val id = long("id").autoIncrement()
    val courseId = long("course_id")
    val title = varchar("title", 255)
    val gitRepositoryId = long("git_repository_id")
    val order = integer("order")

    override val primaryKey = PrimaryKey(id)
}
