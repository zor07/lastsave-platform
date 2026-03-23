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
        telegramBot.sendTextMessage(event.student.telegramChatId, "Новый спринт! Сосредоточимся на «${event.blockTitle}». Твой репозиторий для работы: ${event.repoUrl}")
    }
}
