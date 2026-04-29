package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ReviewJobsTable : Table("review_job") {
    val id = long("id").autoIncrement()
    val githubUsername = varchar("github_username", 255)
    val prUrl = varchar("pr_url", 500)
    val diff = text("diff")
    val status = varchar("status", 20)
    val retryCount = integer("retry_count")
    val createdAt = datetime("created_at")
    val processedAt = datetime("processed_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
