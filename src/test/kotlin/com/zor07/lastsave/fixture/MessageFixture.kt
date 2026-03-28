package com.zor07.lastsave.fixture

import com.zor07.lastsave.enums.MessageSender
import com.zor07.lastsave.enums.WaitFor
import com.zor07.lastsave.model.Message

object MessageFixture {
    fun message(
        id: Long = 1L,
        sectionId: Long = 10L,
        sender: MessageSender = MessageSender.BOT,
        text: String = "text",
        callbackText: String? = null,
        waitFor: WaitFor = WaitFor.NOTHING,
        order: Int = 1,
    ) = Message(
        id = id,
        sectionId = sectionId,
        sender = sender,
        text = text,
        callbackText = callbackText,
        waitFor = waitFor,
        order = order,
    )
}
