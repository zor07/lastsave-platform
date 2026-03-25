package com.zor07.lastsave.model

import com.zor07.lastsave.enums.UnlockCondition

data class NewSection(
    val topicId: Long,
    val title: String,
    val order: Int,
    val unlockCondition: UnlockCondition,
)

data class Section(
    val id: Long,
    val topicId: Long,
    val title: String,
    val order: Int,
    val unlockCondition: UnlockCondition,
)
