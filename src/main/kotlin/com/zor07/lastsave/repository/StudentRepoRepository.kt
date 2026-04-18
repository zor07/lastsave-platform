package com.zor07.lastsave.repository

import com.zor07.lastsave.model.NewStudentRepo
import com.zor07.lastsave.model.StudentRepo
import com.zor07.lastsave.table.StudentRepoTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class StudentRepoRepository {

    fun findAllByStudentId(studentId: Long): List<StudentRepo> =
        StudentRepoTable.selectAll()
            .where { StudentRepoTable.studentId eq studentId }
            .map { it.toStudentRepo() }

    fun deleteAllByStudentId(studentId: Long) {
        StudentRepoTable.deleteWhere { StudentRepoTable.studentId eq studentId }
    }

    fun findByStudentAndGitRepository(studentId: Long, gitRepositoryId: Long): StudentRepo? =
        StudentRepoTable.selectAll()
            .where { (StudentRepoTable.studentId eq studentId) and (StudentRepoTable.gitRepositoryId eq gitRepositoryId) }
            .singleOrNull()
            ?.toStudentRepo()

    fun create(newStudentRepo: NewStudentRepo): StudentRepo {
        val stmt = StudentRepoTable.insert {
            it[studentId] = newStudentRepo.studentId
            it[gitRepositoryId] = newStudentRepo.gitRepositoryId
            it[repoUrl] = newStudentRepo.repoUrl
            it[createdAt] = newStudentRepo.createdAt
        }
        return StudentRepo(
            id = stmt[StudentRepoTable.id],
            studentId = newStudentRepo.studentId,
            gitRepositoryId = newStudentRepo.gitRepositoryId,
            repoUrl = newStudentRepo.repoUrl,
            createdAt = newStudentRepo.createdAt,
        )
    }

    private fun ResultRow.toStudentRepo() = StudentRepo(
        id = this[StudentRepoTable.id],
        studentId = this[StudentRepoTable.studentId],
        gitRepositoryId = this[StudentRepoTable.gitRepositoryId],
        repoUrl = this[StudentRepoTable.repoUrl],
        createdAt = this[StudentRepoTable.createdAt],
    )
}
