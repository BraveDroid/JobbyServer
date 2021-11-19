package com.bravedroid.jobby.auth.domain.services.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.random.Random

@Component
class SecurityUtil(
        private val jwtProvider: JwtProvider,
) {
    private val logger: Logger = LoggerFactory.getLogger(SecurityUtil::class.java)

    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun hashPassword(password: String): String = passwordEncoder.encode(password)

    fun comparePassword(hashedPassword1: String, hashedPassword2: String): Boolean =
            passwordEncoder.matches(hashedPassword1, hashedPassword2)

    fun createJwt(idUser: Long): String = jwtProvider.createJwt(idUser)

    fun decryptUserIdFromJwt(jwt: String): Long = jwtProvider.decryptUserIdFromJwt(jwt)

    fun createRandomString(): String {
        val stringLength = 50

        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..stringLength)
                .map { _ -> Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }

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
