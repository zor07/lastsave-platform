package com.zor07.lastsave.auth

import com.zor07.lastsave.github.GitHubService
import com.zor07.lastsave.student.StudentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@RestController
class GitHubOAuthController(
    private val studentService: StudentService,
    private val gitHubService: GitHubService,
    private val telegramBot: com.zor07.lastsave.bot.TelegramBot,
    private val restTemplate: RestTemplate = RestTemplate(),
    @Value("\${github.client-id}") private val clientId: String,
    @Value("\${github.client-secret}") private val clientSecret: String,
    @Value("\${app.base-url}") private val appBaseUrl: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/auth/github/callback", produces = [MediaType.TEXT_HTML_VALUE])
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String,
    ): String {
        val chatId = state.toLongOrNull()
            ?: throw IllegalArgumentException("Invalid state")

        val accessToken = exchangeCodeForToken(code)
        val user = fetchUser(accessToken)

        val student = studentService.registerStudent(
            telegramChatId = chatId,
            githubUsername = user.username,
            githubName = user.name,
        )

        val repoUrl = gitHubService.createRepoFromTemplate(student.githubUsername)
        gitHubService.addCollaborator(repoUrl.substringAfterLast("/"), student.githubUsername)
        telegramBot.sendRepoLink(chatId, repoUrl)

        logger.info("Registered student ${student.id} for chat $chatId and repo $repoUrl")

        return "<html><body>Авторизация прошла успешно, вернись в Telegram</body></html>"
    }

    private fun exchangeCodeForToken(code: String): String {
        val url = "https://github.com/login/oauth/access_token"
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
        val body = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code", code)
            add("redirect_uri", "$appBaseUrl/auth/github/callback")
        }
        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)
        val token = response.body?.get("access_token") as? String
        return token ?: throw IllegalStateException("No access token in response")
    }

    private fun fetchUser(accessToken: String): GitHubUser {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        }
        val entity = HttpEntity<Void>(headers)
        val response = restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            entity,
            Map::class.java,
        )
        val body = response.body ?: emptyMap<String, Any>()
        val login = body["login"] as? String
        val name = body["name"] as? String ?: ""
        return GitHubUser(username = login ?: "", name = name)
    }

    data class GitHubUser(
        val username: String,
        val name: String,
    )
}
