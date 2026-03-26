package com.zor07.lastsave.service.review

import org.springframework.stereotype.Service

@Service
class AiReviewServiceImpl(
    private val openAiClient: OpenAiClient,
) : AiReviewService {

    override fun review(diff: String): String {
        val prompt = """
            Ты — ревьюер кода на курсе Java Core. Твоя задача — дать студенту полезную обратную связь.

            Правила:
            - Указывай на конкретные проблемы с объяснением почему это проблема
            - Если код хороший — скажи об этом честно
            - Пиши по-русски, дружелюбно но по делу
            - Не переписывай код за студента, направляй вопросами или подсказками
            - Если видишь типичные заблуждения новичка — объясни концепцию
            - Ответ не должен превышать 2000 символов

            Код студента (diff):
            $diff
        """.trimIndent()

        return openAiClient.complete(prompt)
    }
}
