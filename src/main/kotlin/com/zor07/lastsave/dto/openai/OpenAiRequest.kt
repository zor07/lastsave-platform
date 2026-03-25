package com.zor07.lastsave.dto.openai

data class OpenAiRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
)
