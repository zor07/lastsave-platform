package com.zor07.lastsave.course

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "block")
data class Block(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "course_id", nullable = false)
    val courseId: Long,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "template_repo_url", nullable = false)
    val templateRepoUrl: String,

    @Column(name = "\"order\"", nullable = false)
    val order: Int,
)
