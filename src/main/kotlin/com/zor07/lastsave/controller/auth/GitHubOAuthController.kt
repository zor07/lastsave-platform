package com.zor07.lastsave.controller.auth

import com.zor07.lastsave.service.auth.GitHubOAuthService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GitHubOAuthController(
    private val gitHubOAuthService: GitHubOAuthService,
) {

    @GetMapping("/auth/github/callback", produces = [MediaType.TEXT_HTML_VALUE])
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String,
    ): String {
        gitHubOAuthService.processCallback(code, state)
        return SUCCESS_HTML
    }

    companion object {
        private const val SUCCESS_HTML = "<html><body>Авторизация прошла успешно, вернись в Telegram</body></html>"
    }
}
