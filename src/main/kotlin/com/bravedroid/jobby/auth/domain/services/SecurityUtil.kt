package com.bravedroid.jobby.auth.domain.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class SecurityUtil {
    private val encode = Base64.getEncoder().encode("secretToBeStoredInSafePlace".toByteArray())

    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun hashPassword(password: String): String = passwordEncoder.encode(password)

    fun comparePassword(hashedPassword1: String, hashedPassword2: String): Boolean =
            passwordEncoder.matches(hashedPassword1, hashedPassword2)

    fun createJwt(idUser: Long): String {
        return Jwts.builder()
                .setIssuer(idUser.toString())
                .setExpiration(Date(System.currentTimeMillis() + Duration.ofDays(1).toMillis()))
                .signWith(SignatureAlgorithm.HS512, encode)
                .compact()
    }

    fun decryptUserIdFromJwt(jwt: String): Long =
            Jwts.parser().setSigningKey(encode).parseClaimsJws(jwt).body.issuer.toLong()

    fun createRefreshToken(): String {
        val stringLength = 50

        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        val randomString = (1..stringLength)
                .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

        return randomString
    }
}
