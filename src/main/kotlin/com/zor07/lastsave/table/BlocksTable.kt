package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object BlocksTable : Table("block") {
    val id = long("id").autoIncrement()
    val courseId = long("course_id")
    val title = varchar("title", 255)
    val templateRepoName = varchar("template_repo_name", 255)
    val order = integer("order")

    override val primaryKey = PrimaryKey(id)
}
