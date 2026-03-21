package com.zor07.lastsave.student

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "student_progress")
data class StudentProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "section_id", nullable = false)
    val sectionId: Long,

    @Column(name = "pr_url")
    val prUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: StudentProgressStatus,

    @Column(name = "started_at")
    val startedAt: LocalDateTime? = null,

    @Column(name = "completed_at")
    val completedAt: LocalDateTime? = null,
)
