package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.StudentProgress
import com.zor07.lastsave.entity.enums.StudentProgressStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentProgressRepository : JpaRepository<StudentProgress, Long> {
    fun findFirstByStudentIdAndStatusOrderByStartedAtDesc(studentId: Long, status: StudentProgressStatus): StudentProgress?
    fun findByStudentIdAndSectionId(studentId: Long, sectionId: Long): StudentProgress?
}
