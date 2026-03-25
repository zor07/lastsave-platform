package com.zor07.lastsave.model

data class NewTopic(
    val blockId: Long,
    val title: String,
    val order: Int,
)

data class Topic(
    val id: Long,
    val blockId: Long,
    val title: String,
    val order: Int,
)
