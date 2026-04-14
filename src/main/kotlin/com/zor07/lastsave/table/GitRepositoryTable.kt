package com.zor07.lastsave.table

import org.jetbrains.exposed.sql.Table

object GitRepositoryTable : Table("git_repository") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    val description = varchar("description", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
