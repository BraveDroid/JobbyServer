package com.bravedroid.jobby.auth.domain.services.security.util.encryption

import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

@Service
class CipherEncryptionService {

    private companion object {
        //todo will be stored safely
        private const val KEY: String = "aDHN5plfLPH1lNUz3CNhWQ=="
    }

    fun encryptValue(data: String): String {
        val encryptedValue: ByteArray
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey: SecretKey = SecretKeySpec(KEY.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            encryptedValue = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is NoSuchPaddingException,
                is InvalidKeyException,
                is IllegalBlockSizeException,
                is BadPaddingException -> {
                    throw IllegalStateException("data can't be encrypted")
                }
                else -> throw RuntimeException(e)
            }
        }
        return Base64.getEncoder().encodeToString(encryptedValue)
    }

    fun decryptValue(data: String?): String {
        val decryptedValue: ByteArray = try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey: SecretKey = SecretKeySpec(KEY.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher.doFinal(Base64.getDecoder().decode(data))
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is NoSuchPaddingException,
                is InvalidKeyException,
                is IllegalBlockSizeException,
                is BadPaddingException -> {
                    throw IllegalStateException("data can't be decrypted")
                }
                else -> throw RuntimeException(e)
            }
        }
        return String(decryptedValue)
    }
}
