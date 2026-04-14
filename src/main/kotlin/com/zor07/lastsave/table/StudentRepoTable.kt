package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object StudentRepoTable : Table("student_repository") {
    val id = long("id").autoIncrement()
    val studentId = long("student_id")
    val gitRepositoryId = long("git_repository_id")
    val repoUrl = varchar("repo_url", 1024)
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}
