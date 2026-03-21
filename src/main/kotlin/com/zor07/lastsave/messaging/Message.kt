package com.zor07.lastsave.messaging

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "section_id", nullable = false)
    val sectionId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "sender", nullable = false)
    val sender: MessageSender,

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    val text: String,

    @Column(name = "callback_text")
    val callbackText: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "wait_for", nullable = false)
    val waitFor: MessageWaitFor,

    @Column(name = "\"order\"", nullable = false)
    val order: Int,
)
