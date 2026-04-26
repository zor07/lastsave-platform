package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.ProgressStatus
import com.zor07.lastsave.model.BlockTopicPosition
import com.zor07.lastsave.model.NewStudentProgress
import com.zor07.lastsave.model.StudentProgress
import com.zor07.lastsave.table.BlocksTable
import com.zor07.lastsave.table.SectionsTable
import com.zor07.lastsave.table.StudentProgressTable
import com.zor07.lastsave.table.StudentsTable
import com.zor07.lastsave.table.TopicsTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
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

    fun markCompleted(studentId: Long, sectionId: Long) {
        StudentProgressTable.update({
            (StudentProgressTable.studentId eq studentId) and
            (StudentProgressTable.sectionId eq sectionId) and
            (StudentProgressTable.status eq ProgressStatus.IN_PROGRESS.name)
        }) {
            it[status] = ProgressStatus.COMPLETED.name
            it[completedAt] = LocalDateTime.now()
        }
    }

    fun deleteAllByStudentId(studentId: Long) {
        StudentProgressTable.deleteWhere { StudentProgressTable.studentId eq studentId }
    }

    fun findActiveByStudentId(studentId: Long): StudentProgress? =
        StudentProgressTable.selectAll()
            .where { (StudentProgressTable.studentId eq studentId) and (StudentProgressTable.status eq ProgressStatus.IN_PROGRESS.name) }
            .limit(1)
            .singleOrNull()
            ?.toStudentProgress()

    fun findActivePosition(githubUsername: String): BlockTopicPosition? =
        StudentsTable
            .join(StudentProgressTable, JoinType.INNER) { StudentsTable.id eq StudentProgressTable.studentId }
            .join(SectionsTable, JoinType.INNER) { StudentProgressTable.sectionId eq SectionsTable.id }
            .join(TopicsTable, JoinType.INNER) { SectionsTable.topicId eq TopicsTable.id }
            .join(BlocksTable, JoinType.INNER) { TopicsTable.blockId eq BlocksTable.id }
            .select(BlocksTable.order, TopicsTable.order)
            .where {
                (StudentsTable.githubUsername eq githubUsername) and
                (StudentProgressTable.status eq ProgressStatus.IN_PROGRESS.name)
            }
            .limit(1)
            .singleOrNull()
            ?.let { BlockTopicPosition(blockOrder = it[BlocksTable.order], topicOrder = it[TopicsTable.order]) }

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
