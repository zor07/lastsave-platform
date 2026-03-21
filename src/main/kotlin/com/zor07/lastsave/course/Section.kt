package com.zor07.lastsave.course

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "section")
data class Section(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "topic_id", nullable = false)
    val topicId: Long,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "\"order\"", nullable = false)
    val order: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "unlock_condition", nullable = false)
    val unlockCondition: SectionUnlockCondition,
)
