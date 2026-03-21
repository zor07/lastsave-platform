package com.zor07.lastsave.controller.auth

import com.zor07.lastsave.service.auth.GitHubOAuthService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(GitHubOAuthController::class)
class GitHubOAuthControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {

    @MockBean
    private lateinit var gitHubOAuthService: GitHubOAuthService

    @Test
    fun `callback delegates to service and returns html`() {
        mockMvc.get("/auth/github/callback") {
            param("code", "code")
            param("state", "123")
        }
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.TEXT_HTML) }
                content { string("<html><body>Авторизация прошла успешно, вернись в Telegram</body></html>") }
            }

        verify(gitHubOAuthService).processCallback("code", "123")
    }
}
