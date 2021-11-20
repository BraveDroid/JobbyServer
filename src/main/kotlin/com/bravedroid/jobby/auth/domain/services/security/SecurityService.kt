package com.bravedroid.jobby.auth.domain.services.security

import com.bravedroid.jobby.auth.domain.services.security.util.encryption.EncryptionService
import com.bravedroid.jobby.auth.domain.services.security.util.hashing.HashingService
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import kotlin.random.Random

@Service
class SecurityService(
    private val encryptionService: EncryptionService,
    private val hashingService: HashingService,
) {

    fun createRandomString(): String {
        val stringLength = 50
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..stringLength)
            .map { _ -> Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun createRandomSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashPassword(password: String): String = hashingService.hashPassword(password)
    fun createHashedValue(value: String, salt: String): String = hashingService.createHashedValue(value, salt)
    fun comparePassword(hashedPassword1: String, hashedPassword2: String): Boolean =
        hashingService.comparePassword(hashedPassword1, hashedPassword2)

    fun createJwt(idUser: Long): String = encryptionService.createJwtValue(idUser)
    fun decryptUserIdFromJwt(jwt: String): Long = encryptionService.decryptJwtValue(jwt)

    fun getEncryptedUserEmail(data: String): String = encryptionService.encryptCypherValue(data)
    fun getDecryptedUserEmail(data: String): String = encryptionService.decryptCypherValue(data)
}
