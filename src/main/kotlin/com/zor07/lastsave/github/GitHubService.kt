package com.zor07.lastsave.github

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GitHubService(
    private val restTemplate: RestTemplate = RestTemplate(),
    @Value("\${github.token}") private val token: String,
    @Value("\${github.template-repo}") private val templateRepo: String,
    @Value("\${github.org}") private val org: String,
) {

    fun createRepoFromTemplate(githubUsername: String): String {
        val repoName = "${githubUsername}-course"
        val url = "https://api.github.com/repos/$templateRepo/generate"
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
}
