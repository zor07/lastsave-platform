package com.zor07.lastsave.entity

import com.zor07.lastsave.entity.enums.MaterialType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "material")
data class Material(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "section_id", nullable = false)
    val sectionId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: MaterialType,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "url", nullable = false)
    val url: String,

    @Column(name = "\"order\"", nullable = false)
    val order: Int,
)
