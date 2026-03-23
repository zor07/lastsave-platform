package com.zor07.lastsave.service.progress

import com.zor07.lastsave.entity.Section
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress

data class BlockStartResult(
    val progress: StudentProgress,
    val repoUrl: String? = null,
    val blockTitle: String? = null,
)

interface StudentProgressService {
    fun getOrStartProgress(student: Student): StudentProgress
    fun startFirstBlockIfNeeded(student: Student): BlockStartResult?
    fun startNextBlockIfExists(student: Student, currentSection: Section): BlockStartResult?
    fun completeSectionAndAdvance(student: Student, currentSectionId: Long): BlockStartResult?
}
