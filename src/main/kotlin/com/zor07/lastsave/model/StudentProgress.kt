package com.zor07.lastsave.model

import com.zor07.lastsave.enums.ProgressStatus
import java.time.LocalDateTime

data class NewStudentProgress(
    val studentId: Long,
    val sectionId: Long,
    val status: ProgressStatus,
    val startedAt: LocalDateTime,
)

data class StudentProgress(
    val id: Long,
    val studentId: Long,
    val sectionId: Long,
    val prUrl: String?,
    val status: ProgressStatus,
    val startedAt: LocalDateTime?,
    val completedAt: LocalDateTime?,
)

data class BlockTopicPosition(
    val blockCode: String,
    val topicCode: String,
)
