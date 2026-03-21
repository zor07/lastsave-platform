package com.zor07.lastsave.service.auth

import com.zor07.lastsave.dto.github.GitHubUser
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.service.StudentService
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.github.GitHubOAuthClient
import com.zor07.lastsave.service.github.GitHubService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GitHubOAuthServiceImplTest {

    @Mock
    private lateinit var studentService: StudentService

    @Mock
    private lateinit var gitHubService: GitHubService

    @Mock
    private lateinit var telegramBot: TelegramBot

    @Mock
    private lateinit var gitHubOAuthClient: GitHubOAuthClient

    @InjectMocks
    private lateinit var service: GitHubOAuthServiceImpl

    @Test
    fun `processCallback registers student and sends repo link`() {
        val code = "code"
        val state = "123"
        val token = "token"
        val user = GitHubUser(username = "octocat", name = "Octo Cat")
        val student = Student(
            id = 1L,
            telegramChatId = 123L,
            githubUsername = "octocat",
            githubName = "Octo Cat",
        )

        given(gitHubOAuthClient.exchangeCodeForToken(code)).willReturn(token)
        given(gitHubOAuthClient.fetchUser(token)).willReturn(user)
        given(studentService.registerStudent(123L, "octocat", "Octo Cat")).willReturn(student)
        given(gitHubService.createRepoFromTemplate("octocat")).willReturn("https://github.com/org/repo")

        service.processCallback(code, state)

        verify(gitHubService).addCollaborator("repo", "octocat")
        verify(telegramBot).sendRepoLink(123L, "https://github.com/org/repo")
    }

    @Test
    fun `processCallback throws on invalid state`() {
        assertThrows(IllegalArgumentException::class.java) {
            service.processCallback("code", "invalid")
        }

        verifyNoInteractions(gitHubOAuthClient, studentService, gitHubService, telegramBot)
    }
}
