package com.zor07.lastsave.service.review

import com.fasterxml.jackson.databind.ObjectMapper
import com.zor07.lastsave.dto.openai.OpenAiMessage
import com.zor07.lastsave.dto.openai.OpenAiRequest
import com.zor07.lastsave.dto.openai.OpenAiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OpenAiClient(
    @Qualifier("openAiRestTemplate")
    private val restTemplate: RestTemplate,
    @Value("\${openai.api-key}") private val apiKey: String,
    @Value("\${openai.base-url}") private val baseUrl: String,
    @Value("\${openai.model}") private val model: String,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun complete(prompt: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        }
        val body = OpenAiRequest(
            model = model,
            messages = listOf(OpenAiMessage(role = "user", content = prompt)),
        )
        logger.info("Sending request to OpenAI: url=$baseUrl/chat/completions, model=$model, promptLength=${prompt.length}")
        logger.info(
            "OpenAI request details:\n  URL: $baseUrl/chat/completions\n  Headers: {}\n  Body: {}",
            headers.map { (k, v) -> "$k: ${if (k == HttpHeaders.AUTHORIZATION) "Bearer ***" else v}" },
            objectMapper.writeValueAsString(body)
        )

        val response = restTemplate.postForEntity(
            "$baseUrl/chat/completions",
            HttpEntity(body, headers),
            OpenAiResponse::class.java,
        )

        val content = response.body?.choices?.firstOrNull()?.message?.content
        if (content.isNullOrBlank()) {
            logger.error("OpenAI returned empty response")
            throw IllegalStateException("Empty response from OpenAI")
        }
        logger.info("OpenAI response: contentLength=${content.length}\n$content")
        return content
    }
}
