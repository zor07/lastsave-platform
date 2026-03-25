package com.zor07.lastsave.model

import com.zor07.lastsave.enums.MessageSender
import com.zor07.lastsave.enums.WaitFor

data class NewMessage(
    val sectionId: Long,
    val sender: MessageSender,
    val text: String,
    val callbackText: String?,
    val waitFor: WaitFor,
    val order: Int,
)

data class Message(
    val id: Long,
    val sectionId: Long,
    val sender: MessageSender,
    val text: String,
    val callbackText: String?,
    val waitFor: WaitFor,
    val order: Int,
)
