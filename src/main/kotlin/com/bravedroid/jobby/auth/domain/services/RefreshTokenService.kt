package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.RefreshTokenException
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import com.bravedroid.jobby.auth.domain.services.security.SecurityService
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val securityService: SecurityService,
) {
    fun createRefreshToken(user: User): String {
        val randomString = securityService.createRandomString()
        val userEmail = user.email
        val encryptedUserEmail = securityService.getEncryptedUserEmail(userEmail)
        val refreshToken = "$randomString.$encryptedUserEmail"
        val salt = securityService.createRandomSalt()
        val hashedRefreshToken = securityService.createHashedValue(refreshToken, salt)
        refreshTokenRepository.saveHashedRefreshToken(hashedRefreshToken, salt, user)
        return refreshToken
    }

    fun getByUser(user: User): RefreshToken? {
        return refreshTokenRepository.findByUser(user)
    }

    fun isValidateRefreshTokenFormat(refreshToken: String): Boolean =
        refreshToken.toByteArray().size >= 50 && refreshToken.contains('.')

    fun getEmailFromRefreshToken(refreshToken: String): String {
        val hashedEmail = refreshToken.substringAfter('.')
        try {
            return securityService.getDecryptedUserEmail(hashedEmail)
        } catch (e: Exception) {
            throw RefreshTokenException(refreshToken, "Unknown User")
        }
    }

    fun isRefreshTokenExist(user: User, refreshTokenFromHeader: String): Boolean {
        val randomString = refreshTokenFromHeader.substringBefore('.')
        val encryptedUserEmail = refreshTokenFromHeader.substringAfter('.')
        val refreshToken = "$randomString.$encryptedUserEmail"
        val list = refreshTokenRepository.findAllRefreshTokenByUser(user)
        return list.any {
            val salt = it.salt
            val hashedRefreshToken = securityService.createHashedValue(refreshToken, salt)
            hashedRefreshToken == it.hashedToken
        }
    }
}
