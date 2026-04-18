package com.zor07.lastsave.service.github

interface GitHubSecretEncryptor {
    fun encrypt(publicKey: String, secretValue: String): String
}
