package com.zor07.lastsave.service.ci

interface CiService {
    fun getTestTag(githubUsername: String): String?
}
