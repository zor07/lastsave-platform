package com.zor07.lastsave.event

import com.zor07.lastsave.service.bot.TelegramBot
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BlockStartedEventListener(
    private val telegramBot: TelegramBot,
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onBlockStarted(event: BlockStartedEvent) {
        telegramBot.sendRepoLink(event.student.telegramChatId, event.repoUrl, event.blockTitle)
    }
}
