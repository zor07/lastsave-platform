package com.zor07.lastsave.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "students")
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "telegram_chat_id", nullable = false, unique = true)
    val telegramChatId: Long,

    @Column(name = "github_username", nullable = false, unique = true)
    val githubUsername: String,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
