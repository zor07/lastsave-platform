package com.zor07.lastsave.messaging

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "message_log")
data class MessageLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "message_id", nullable = false)
    val messageId: Long,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "sent_at", nullable = false)
    val sentAt: LocalDateTime,

    @Column(name = "callback_received_at")
    val callbackReceivedAt: LocalDateTime? = null,
)
