package com.zor07.lastsave.service.progress

import com.zor07.lastsave.entity.Section
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.entity.StudentProgress

interface BlockProgressService {
    fun startFirstBlockIfNeeded(student: Student): StudentProgress?
    fun startNextBlockIfExists(student: Student, currentSection: Section): StudentProgress?
}
