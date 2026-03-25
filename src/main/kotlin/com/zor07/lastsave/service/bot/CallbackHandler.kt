package com.zor07.lastsave.service.bot

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CallbackHandler {

    // TODO: implement in block 3 (completeSectionAndAdvance on callback)
    @EventListener
    fun handle(event: TelegramCallbackEvent) {
    }
}
