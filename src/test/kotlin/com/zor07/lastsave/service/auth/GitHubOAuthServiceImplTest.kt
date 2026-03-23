package com.zor07.lastsave.service.auth

import com.zor07.lastsave.dto.github.GitHubUser
import com.zor07.lastsave.entity.Student
import com.zor07.lastsave.service.github.GitHubOAuthClient
import com.zor07.lastsave.service.progress.StudentProgressService
import com.zor07.lastsave.service.progress.BlockStartResult
import com.zor07.lastsave.service.bot.TelegramBot
import com.zor07.lastsave.service.student.StudentService
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
    private lateinit var gitHubOAuthClient: GitHubOAuthClient

    @Mock
    private lateinit var studentProgressService: StudentProgressService

    @Mock
    private lateinit var telegramBot: TelegramBot

    @InjectMocks
    private lateinit var service: GitHubOAuthServiceImpl

    @Test
    fun `processCallback registers student`() {
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
        val progress = com.zor07.lastsave.entity.StudentProgress(
            id = 2L,
            studentId = 1L,
            sectionId = 10L,
            status = com.zor07.lastsave.entity.enums.StudentProgressStatus.IN_PROGRESS,
        )
        given(studentProgressService.startFirstBlockIfNeeded(student)).willReturn(
            BlockStartResult(
                progress = progress,
                repoUrl = "https://github.com/org/repo",
                blockTitle = "block",
            ),
        )

        service.processCallback(code, state)

        verify(studentService).registerStudent(123L, "octocat", "Octo Cat")
        verify(studentProgressService).startFirstBlockIfNeeded(student)
        verify(telegramBot).sendRepoLink(123L, "https://github.com/org/repo", "block")
    }

    @Test
    fun `processCallback throws on invalid state`() {
        assertThrows(IllegalArgumentException::class.java) {
            service.processCallback("code", "invalid")
        }

        verifyNoInteractions(gitHubOAuthClient, studentService)
    }
}
