package com.zor07.lastsave.service.github

import com.zor07.lastsave.annotation.ForLocalTesting
import com.zor07.lastsave.dto.github.RepoPublicKeyResponse
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
    @Value("\${github.pr-token}") private val prToken: String,
    @Value("\${github.org}") private val org: String,
    @Value("\${app.base-url}") private val appBaseUrl: String,
    @Value("\${review.secret-token}") private val reviewSecretToken: String,
    private val secretEncryptor: GitHubSecretEncryptor,
) {

    fun createRepoFromTemplate(
        templateRepoName: String,
        collaboratorGithubUsername: String
    ): String {
        val newRepoName = "$templateRepoName-$collaboratorGithubUsername"
        val url = "https://api.github.com/repos/$org/$templateRepoName/generate"

        val headers = authHeaders().apply {
            add("Accept", "application/vnd.github.baptiste-preview+json")
        }
        val body = mapOf(
            "owner" to org,
            "name" to newRepoName,
            "private" to true,
        )
        val request = HttpEntity(body, headers)
        restTemplate.postForEntity(url, request, Map::class.java)
        addCollaborator(newRepoName, collaboratorGithubUsername)
        addRepoSecrets(newRepoName)
        return "https://github.com/$org/$newRepoName"
    }

    private fun addRepoSecrets(repoName: String) {
        addSecret(repoName, "APP_BASE_URL", appBaseUrl)
        addSecret(repoName, "CI_PR_WEBHOOK_TOKEN", reviewSecretToken)
    }

    private fun addSecret(repoName: String, secretName: String, secretValue: String) {
        val publicKey = getRepoPublicKey(repoName)
        val encryptedValue = secretEncryptor.encrypt(publicKey.key, secretValue)
        val url = "https://api.github.com/repos/$org/$repoName/actions/secrets/$secretName"
        val body = mapOf(
            "encrypted_value" to encryptedValue,
            "key_id" to publicKey.keyId,
        )
        restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(body, authHeaders()), Void::class.java)
    }

    private fun getRepoPublicKey(repoName: String): RepoPublicKeyResponse {
        val url = "https://api.github.com/repos/$org/$repoName/actions/secrets/public-key"
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Void>(authHeaders()), RepoPublicKeyResponse::class.java).body!!
    }

    @ForLocalTesting
    fun getPrDiff(owner: String, repo: String, prNumber: Int): String {
        val url = "https://api.github.com/repos/$owner/$repo/pulls/$prNumber"
        val headers = prAuthHeaders().apply {
            add("Accept", "application/vnd.github.v3.diff")
        }
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Void>(headers), String::class.java).body!!
    }

    fun deleteRepo(repoName: String) {
        val url = "https://api.github.com/repos/$org/$repoName"
        restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity<Void>(authHeaders()), Void::class.java)
    }

    private fun addCollaborator(repoName: String, githubUsername: String) {
        val url = "https://api.github.com/repos/$org/$repoName/collaborators/$githubUsername"
        val request = HttpEntity(emptyMap<String, Any>(), authHeaders())
        restTemplate.exchange(url, HttpMethod.PUT, request, Void::class.java)
    }

    private fun authHeaders(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }

    private fun prAuthHeaders(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            add(HttpHeaders.AUTHORIZATION, "Bearer $prToken")
        }
}
