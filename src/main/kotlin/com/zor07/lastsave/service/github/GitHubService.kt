package com.zor07.lastsave.service.github

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GitHubService(
    @Qualifier("gitHubRestTemplate")
    private val restTemplate: RestTemplate,
    @Value("\${github.token}") private val token: String,
    @Value("\${github.org}") private val org: String,
) {

    fun createRepoFromTemplate(templateRepoUrl: String, repoName: String): String {
        val (templateOwner, templateName) = parseTemplate(templateRepoUrl)
        val url = "https://api.github.com/repos/$templateOwner/$templateName/generate"
        val headers = authHeaders().apply {
            add("Accept", "application/vnd.github.baptiste-preview+json")
        }
        val body = mapOf(
            "owner" to org,
            "name" to repoName,
            "private" to true,
        )
        val request = HttpEntity(body, headers)
        restTemplate.postForEntity(url, request, Map::class.java)
        return "https://github.com/$org/$repoName"
    }

    fun addCollaborator(repoName: String, githubUsername: String) {
        val url = "https://api.github.com/repos/$org/$repoName/collaborators/$githubUsername"
        val headers = authHeaders()
        val request = HttpEntity(emptyMap<String, Any>(), headers)
        restTemplate.exchange(url, HttpMethod.PUT, request, Void::class.java)
    }

    private fun authHeaders(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }

    private fun parseTemplate(templateRepoUrl: String): Pair<String, String> {
        val cleaned = templateRepoUrl.removeSuffix(".git")
        val parts = cleaned.substringAfter("github.com/").split("/")
        if (parts.size < 2) {
            throw IllegalArgumentException("Invalid template repo url: $templateRepoUrl")
        }
        return parts[0] to parts[1]
    }
}
