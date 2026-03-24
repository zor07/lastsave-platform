package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.StudentProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StudentProgressRepository : JpaRepository<StudentProgress, Long> {
    @Query(
        value = """
            select * from student_progress
            where student_id = :studentId
              and status = :status
            order by started_at desc
            limit 1
        """,
        nativeQuery = true,
    )
    fun findFirstByStudentIdAndStatusOrderByStartedAtDesc(
        @Param("studentId") studentId: Long,
        @Param("status") status: String,
    ): StudentProgress?

    @Query(
        value = """
            select * from student_progress
            where student_id = :studentId
              and section_id = :sectionId
            limit 1
        """,
        nativeQuery = true,
    )
    fun findByStudentIdAndSectionId(
        @Param("studentId") studentId: Long,
        @Param("sectionId") sectionId: Long,
    ): StudentProgress?
}
