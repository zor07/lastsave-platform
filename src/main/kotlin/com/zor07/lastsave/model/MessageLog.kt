package com.zor07.lastsave.model

import java.time.LocalDateTime

data class NewMessageLog(
    val messageId: Long,
    val studentId: Long,
    val sentAt: LocalDateTime,
)

data class MessageLog(
    val id: Long,
    val messageId: Long,
    val studentId: Long,
    val sentAt: LocalDateTime,
    val callbackReceivedAt: LocalDateTime?,
    val prReceivedAt: LocalDateTime?,
)
