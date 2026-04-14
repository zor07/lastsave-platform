package com.zor07.lastsave.service.bot

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
class TelegramBot(
    private val eventPublisher: ApplicationEventPublisher,
    @Value("\${telegram.bot.token}") private val token: String,
    @Value("\${telegram.bot.username}") private val username: String,
    @Value("\${telegram.bot.proxy.host:}") proxyHost: String,
    @Value("\${telegram.bot.proxy.port:0}") proxyPort: Int,
) : TelegramLongPollingBot(buildOptions(proxyHost, proxyPort), token) {

    companion object {
        private fun buildOptions(proxyHost: String, proxyPort: Int): DefaultBotOptions =
            DefaultBotOptions().apply {
                if (proxyHost.isNotEmpty()) {
                    this.proxyHost = proxyHost
                    this.proxyPort = proxyPort
                    this.proxyType = DefaultBotOptions.ProxyType.SOCKS5
                }
            }
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername(): String = username

    override fun onUpdateReceived(update: Update?) {
        val safeUpdate = requireNotNull(update) { "Update must not be null" }
        safeUpdate.message?.let { eventPublisher.publishEvent(TelegramMessageEvent(it)) }
        safeUpdate.callbackQuery?.let { callback ->
            eventPublisher.publishEvent(TelegramCallbackEvent(callback))
        }
    }

    fun sendTextMessage(chatId: Long, text: String) {
        try {
            execute(SendMessage(chatId.toString(), text))
        } catch (ex: Exception) {
            logger.error("Failed to send message to chatId={}", chatId, ex)
        }
    }

    fun sendMessageWithButton(chatId: Long, text: String, buttonText: String, callbackData: String) {
        val keyboard = InlineKeyboardMarkup(listOf(listOf(
            InlineKeyboardButton(buttonText).apply { this.callbackData = callbackData }
        )))
        try {
            execute(SendMessage(chatId.toString(), text).apply { replyMarkup = keyboard })
        } catch (ex: Exception) {
            logger.error("Failed to send message with button to chatId={}", chatId, ex)
        }
    }

    fun removeInlineKeyboard(chatId: Long, messageId: Int) {
        try {
            execute(EditMessageReplyMarkup().apply {
                this.chatId = chatId.toString()
                this.messageId = messageId
                this.replyMarkup = InlineKeyboardMarkup(emptyList())
            })
        } catch (ex: Exception) {
            logger.error("Failed to remove keyboard from chatId={}, messageId={}", chatId, messageId, ex)
        }
    }
}
