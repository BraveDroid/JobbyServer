package com.bravedroid.jobby.auth.domain.services.security.util.encryption

import com.bravedroid.jobby.auth.domain.exceptions.AccessTokenException
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtEncryptionService(
    private val jwtDateProvider: JwtDateProvider,
) {
    private companion object {
        //todo will be stored safely
        private const val KEY: String = "secretToBeStoredInSafePlace"
    }

    private val logger: Logger = LoggerFactory.getLogger(JwtEncryptionService::class.java)
    private val secretByteArray = Base64.getEncoder().encode(KEY.toByteArray())

    fun createJwt(idUser: Long): String = Jwts.builder()
        .setIssuer(idUser.toString())
        .setExpiration(jwtDateProvider.provideExpirationDayForAccessToken())
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
