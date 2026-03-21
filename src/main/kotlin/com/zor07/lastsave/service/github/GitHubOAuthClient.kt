package com.zor07.lastsave.service.github

import com.zor07.lastsave.dto.github.GitHubAccessTokenResponse
import com.zor07.lastsave.dto.github.GitHubUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class GitHubOAuthClient(
    @Qualifier("gitHubRestTemplate")
    private val restTemplate: RestTemplate,
    @Value("\${github.client-id}") private val clientId: String,
    @Value("\${github.client-secret}") private val clientSecret: String,
    @Value("\${app.base-url}") private val appBaseUrl: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun exchangeCodeForToken(code: String): String {
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

        val response = restTemplate.postForEntity(
            url,
            HttpEntity(body, headers),
            GitHubAccessTokenResponse::class.java,
        )
        val token = response.body?.accessToken
        if (token.isNullOrBlank()) {
            logger.error("GitHub OAuth: empty access token for code {}", code)
            throw IllegalStateException("No access token in response")
        }
        return token
    }

    fun fetchUser(accessToken: String): GitHubUser {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        }
        val response = restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            Map::class.java,
        )
        val body = response.body as? Map<*, *> ?: emptyMap<Any, Any>()
        val login = body["login"] as? String
        val name = body["name"] as? String ?: ""
        if (login.isNullOrBlank()) {
            logger.error("GitHub OAuth: empty login in user response")
            throw IllegalStateException("No login in user response")
        }
        return GitHubUser(username = login, name = name)
    }
}
