package com.bravedroid.jobby.auth.domain.services.utils

import com.bravedroid.jobby.auth.domain.exceptions.AccessTokenException
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider(
        private val dateProvider: DateProvider,
) {
    private val logger: Logger = LoggerFactory.getLogger(JwtProvider::class.java)

    private val secretByteArray = Base64.getEncoder().encode("secretToBeStoredInSafePlace".toByteArray())

    fun createJwt(idUser: Long): String = Jwts.builder()
            .setIssuer(idUser.toString())
            .setExpiration(dateProvider.provideExpirationDayForAccessToken())
            .signWith(SignatureAlgorithm.HS512, secretByteArray)
            .compact()


    fun decryptUserIdFromJwt(jwt: String): Long = try {
        Jwts.parser().setSigningKey(secretByteArray)
                .parseClaimsJws(jwt).body.issuer.toLong()
    } catch (e: Exception) {
        logger.error(e.localizedMessage)
        when (e) {
            is ExpiredJwtException, is UnsupportedJwtException, is MalformedJwtException, is SignatureException -> {
                throw AccessTokenException()
            }
            else -> {
                throw RuntimeException(e)
            }
        }
    }
}
