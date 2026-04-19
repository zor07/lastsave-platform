package com.zor07.lastsave.repository

import com.zor07.lastsave.model.GitRepository
import com.zor07.lastsave.table.GitRepositoryTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class GitRepositoryRepository {

    fun findAll(): List<GitRepository> =
        GitRepositoryTable.selectAll().map { it.toGitRepository() }

    fun findById(id: Long): GitRepository? =
        GitRepositoryTable.selectAll()
            .where { GitRepositoryTable.id eq id }
            .singleOrNull()
            ?.toGitRepository()

    private fun ResultRow.toGitRepository() = GitRepository(
        id = this[GitRepositoryTable.id],
        name = this[GitRepositoryTable.name],
        description = this[GitRepositoryTable.description],
    )
}
