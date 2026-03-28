package com.zor07.lastsave.service.review

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zor07.lastsave.dto.openai.OpenAiResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class OpenAiClientTest {

    private val objectMapper = jacksonObjectMapper()
    private val mockRestTemplate: RestTemplate = mock()

    private val client = OpenAiClient(
        restTemplate = mockRestTemplate,
        apiKey = "test-key",
        baseUrl = "https://api.proxyapi.ru/openai/v1",
        model = "gpt-4o",
        objectMapper = objectMapper,
    )

    @Test
    fun `complete returns content from OpenAI response`() {
        val json = """
            {
                "id": "chatcmpl-DNhryVR0HNKeEMoArM2ccz82mRApt",
                "object": "chat.completion",
                "created": 1774542550,
                "model": "gpt-4o-2024-11-20",
                "choices": [
                    {
                        "index": 0,
                        "message": {
                            "role": "assistant",
                            "content": "конструктивную и подробную обратную связь",
                            "refusal": null,
                            "annotations": []
                        },
                        "logprobs": null,
                        "finish_reason": "stop"
                    }
                ],
                "usage": {
                    "prompt_tokens": 31,
                    "completion_tokens": 121,
                    "total_tokens": 152
                },
                "service_tier": "default",
                "system_fingerprint": "fp_94129f02ea"
            }
        """.trimIndent()

        val parsedResponse = objectMapper.readValue(json, OpenAiResponse::class.java)

        whenever(mockRestTemplate.postForEntity(
            eq("https://api.proxyapi.ru/openai/v1/chat/completions"),
            any(),
            eq(OpenAiResponse::class.java),
        )).thenReturn(ResponseEntity.ok(parsedResponse))

        val result = client.complete("test prompt")

        assertThat(result).isEqualTo("конструктивную и подробную обратную связь")
    }
}
