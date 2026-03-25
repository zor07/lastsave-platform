package com.zor07.lastsave.model

import com.zor07.lastsave.enums.MaterialType

data class NewMaterial(
    val sectionId: Long,
    val type: MaterialType,
    val title: String,
    val url: String,
    val order: Int,
)

data class Material(
    val id: Long,
    val sectionId: Long,
    val type: MaterialType,
    val title: String,
    val url: String,
    val order: Int,
)
