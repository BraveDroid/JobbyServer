package com.bravedroid.jobby.auth.domain.services.security.util.hashing

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Service
class HashingService {
    private val logger: Logger = LoggerFactory.getLogger(HashingService::class.java)
    private val bCryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun hashPassword(password: String): String = bCryptPasswordEncoder.encode(password)

    fun comparePassword(hashedPassword1: String, hashedPassword2: String): Boolean =
        bCryptPasswordEncoder.matches(hashedPassword1, hashedPassword2)

    fun createHashedValue(value: String, salt: String): String {
        var hashedValue: ByteArray? = null
        val spec: KeySpec = PBEKeySpec(value.toCharArray(), salt.toByteArray(), 5000, 128)
        try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            hashedValue = factory.generateSecret(spec).encoded
        } catch (e: InvalidKeySpecException) {
            logger.error(e.message)
        } catch (e: NoSuchAlgorithmException) {
            logger.error(e.message)
        }
        return Base64.getEncoder().encodeToString(hashedValue)
    }
}
