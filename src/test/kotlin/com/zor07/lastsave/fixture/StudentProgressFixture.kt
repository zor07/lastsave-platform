package com.zor07.lastsave.fixture

import com.zor07.lastsave.enums.ProgressStatus
import com.zor07.lastsave.model.StudentProgress
import java.time.LocalDateTime

object StudentProgressFixture {
    fun studentProgress(
        id: Long = 1L,
        studentId: Long = 1L,
        sectionId: Long = 10L,
        prUrl: String? = null,
        status: ProgressStatus = ProgressStatus.IN_PROGRESS,
        startedAt: LocalDateTime? = LocalDateTime.now(),
        completedAt: LocalDateTime? = null,
    ) = StudentProgress(
        id = id,
        studentId = studentId,
        sectionId = sectionId,
        prUrl = prUrl,
        status = status,
        startedAt = startedAt,
        completedAt = completedAt,
    )
}
