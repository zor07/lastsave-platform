package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.ProgressStatus
import com.zor07.lastsave.model.NewStudent
import com.zor07.lastsave.model.Student
import com.zor07.lastsave.table.StudentProgressTable
import com.zor07.lastsave.table.StudentsTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class StudentRepository {

    fun save(newStudent: NewStudent): Student {
        val now = LocalDateTime.now()
        val stmt = StudentsTable.insert {
            it[telegramChatId] = newStudent.telegramChatId
            it[githubUsername] = newStudent.githubUsername
            it[createdAt] = now
        }
        return Student(
            id = stmt[StudentsTable.id],
            telegramChatId = newStudent.telegramChatId,
            githubUsername = newStudent.githubUsername,
            createdAt = now,
        )
    }

    fun findAllWithActiveProgress(): List<Student> =
        StudentsTable
            .join(StudentProgressTable, JoinType.INNER, StudentsTable.id, StudentProgressTable.studentId)
            .selectAll()
            .where { StudentProgressTable.status eq ProgressStatus.IN_PROGRESS.name }
            .map { it.toStudent() }

    fun findByChatId(chatId: Long): Student? =
        StudentsTable.selectAll()
            .where { StudentsTable.telegramChatId eq chatId }
            .singleOrNull()
            ?.toStudent()

    private fun ResultRow.toStudent() = Student(
        id = this[StudentsTable.id],
        telegramChatId = this[StudentsTable.telegramChatId],
        githubUsername = this[StudentsTable.githubUsername],
        createdAt = this[StudentsTable.createdAt],
    )
}
