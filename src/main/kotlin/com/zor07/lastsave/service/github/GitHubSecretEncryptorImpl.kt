package com.zor07.lastsave.service.github

import com.goterl.lazysodium.LazySodiumJava
import com.goterl.lazysodium.SodiumJava
import com.goterl.lazysodium.interfaces.Box
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class GitHubSecretEncryptorImpl : GitHubSecretEncryptor {

    override fun encrypt(publicKey: String, secretValue: String): String {
        val sodium = LazySodiumJava(SodiumJava())
        val publicKeyBytes = Base64.getDecoder().decode(publicKey)
        val messageBytes = secretValue.toByteArray(Charsets.UTF_8)
        val cipherText = ByteArray(Box.SEALBYTES + messageBytes.size)
        sodium.cryptoBoxSeal(cipherText, messageBytes, messageBytes.size.toLong(), publicKeyBytes)
        return Base64.getEncoder().encodeToString(cipherText)
    }
}
