package com.zor07.lastsave.service.github

interface GitHubOAuthService {
    fun processCallback(code: String, state: String)
}
