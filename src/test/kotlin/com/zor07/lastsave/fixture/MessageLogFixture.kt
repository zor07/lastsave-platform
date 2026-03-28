package com.zor07.lastsave.fixture

import com.zor07.lastsave.model.MessageLog
import java.time.LocalDateTime

object MessageLogFixture {
    fun messageLog(
        id: Long = 1L,
        messageId: Long = 1L,
        studentId: Long = 1L,
        sentAt: LocalDateTime = LocalDateTime.now(),
        callbackReceivedAt: LocalDateTime? = null,
        prReceivedAt: LocalDateTime? = null,
    ) = MessageLog(
        id = id,
        messageId = messageId,
        studentId = studentId,
        sentAt = sentAt,
        callbackReceivedAt = callbackReceivedAt,
        prReceivedAt = prReceivedAt,
    )
}
