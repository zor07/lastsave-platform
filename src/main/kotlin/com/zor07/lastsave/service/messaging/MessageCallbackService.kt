package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.service.progress.BlockStartResult

interface MessageCallbackService {
    fun handleCallback(chatId: Long, messageId: Long): BlockStartResult?
}
