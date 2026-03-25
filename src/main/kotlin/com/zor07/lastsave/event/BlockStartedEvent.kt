package com.zor07.lastsave.event

import com.zor07.lastsave.model.Student

data class BlockStartedEvent(
    val student: Student,
    val repoUrl: String,
    val blockTitle: String,
)
