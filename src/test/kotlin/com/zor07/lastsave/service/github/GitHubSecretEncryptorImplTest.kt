package com.zor07.lastsave.service.github

import com.goterl.lazysodium.LazySodiumJava
import com.goterl.lazysodium.SodiumJava
import com.goterl.lazysodium.interfaces.Box
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Base64

class GitHubSecretEncryptorImplTest {

    private val encryptor = GitHubSecretEncryptorImpl()

    @Test
    fun `encrypted value can be decrypted with the corresponding private key`() {
        val sodium = LazySodiumJava(SodiumJava())

        val publicKeyBytes = ByteArray(Box.PUBLICKEYBYTES)
        val privateKeyBytes = ByteArray(Box.SECRETKEYBYTES)
        sodium.cryptoBoxKeypair(publicKeyBytes, privateKeyBytes)

        val publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes)
        val secretValue = "super-secret-value"

        val encryptedBase64 = encryptor.encrypt(publicKeyBase64, secretValue)

        val cipherText = Base64.getDecoder().decode(encryptedBase64)
        val decrypted = ByteArray(cipherText.size - Box.SEALBYTES)
        sodium.cryptoBoxSealOpen(decrypted, cipherText, cipherText.size.toLong(), publicKeyBytes, privateKeyBytes)

        assertEquals(secretValue, String(decrypted, Charsets.UTF_8))
    }
}
