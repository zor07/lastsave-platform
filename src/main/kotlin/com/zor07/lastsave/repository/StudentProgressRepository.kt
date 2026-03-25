package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.ProgressStatus
import com.zor07.lastsave.model.NewStudentProgress
import com.zor07.lastsave.model.StudentProgress
import com.zor07.lastsave.table.StudentProgressTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class StudentProgressRepository {

    fun save(newProgress: NewStudentProgress): StudentProgress {
        val stmt = StudentProgressTable.insert {
            it[studentId] = newProgress.studentId
            it[sectionId] = newProgress.sectionId
            it[status] = newProgress.status.name
            it[startedAt] = newProgress.startedAt
        }
        return StudentProgress(
            id = stmt[StudentProgressTable.id],
            studentId = newProgress.studentId,
            sectionId = newProgress.sectionId,
            prUrl = null,
            status = newProgress.status,
            startedAt = newProgress.startedAt,
            completedAt = null,
        )
    }

    fun findActiveByStudentId(studentId: Long): StudentProgress? =
        StudentProgressTable.selectAll()
            .where { (StudentProgressTable.studentId eq studentId) and (StudentProgressTable.status eq ProgressStatus.IN_PROGRESS.name) }
            .limit(1)
            .singleOrNull()
            ?.toStudentProgress()

    private fun ResultRow.toStudentProgress() = StudentProgress(
        id = this[StudentProgressTable.id],
        studentId = this[StudentProgressTable.studentId],
        sectionId = this[StudentProgressTable.sectionId],
        prUrl = this[StudentProgressTable.prUrl],
        status = ProgressStatus.valueOf(this[StudentProgressTable.status]),
        startedAt = this[StudentProgressTable.startedAt],
        completedAt = this[StudentProgressTable.completedAt],
    )
}
