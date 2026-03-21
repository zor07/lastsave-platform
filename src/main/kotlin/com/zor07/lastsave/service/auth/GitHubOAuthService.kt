package com.zor07.lastsave.service.auth

interface GitHubOAuthService {
    fun processCallback(code: String, state: String)
}
