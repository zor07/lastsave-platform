package com.zor07.lastsave.model

data class NewBlock(
    val courseId: Long,
    val title: String,
    val gitRepositoryId: Long?,
    val order: Int,
)

data class Block(
    val id: Long,
    val courseId: Long,
    val title: String,
    val gitRepositoryId: Long?,
    val order: Int,
)
