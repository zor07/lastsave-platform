package com.zor07.lastsave.model

import java.time.LocalDateTime

data class NewStudentRepo(
    val studentId: Long,
    val gitRepositoryId: Long,
    val repoUrl: String,
    val createdAt: LocalDateTime,
)

data class StudentRepo(
    val id: Long,
    val studentId: Long,
    val gitRepositoryId: Long,
    val repoUrl: String,
    val createdAt: LocalDateTime,
)
