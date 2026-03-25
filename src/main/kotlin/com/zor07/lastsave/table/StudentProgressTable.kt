package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object StudentProgressTable : Table("student_progress") {
    val id = long("id").autoIncrement()
    val studentId = long("student_id")
    val sectionId = long("section_id")
    val prUrl = varchar("pr_url", 1024).nullable()
    val status = varchar("status", 50)
    val startedAt = datetime("started_at").nullable()
    val completedAt = datetime("completed_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
