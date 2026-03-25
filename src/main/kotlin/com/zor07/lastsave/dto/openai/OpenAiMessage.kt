package com.zor07.lastsave.dto.openai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiMessage(
    val role: String,
    val content: String,
)
