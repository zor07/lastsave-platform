package com.zor07.lastsave.service.messaging

interface MessageCallbackService {
    fun handleCallback(chatId: Long, messageId: Long, callbackQueryId: String)
}
