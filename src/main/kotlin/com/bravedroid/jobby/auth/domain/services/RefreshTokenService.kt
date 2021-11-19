package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import com.bravedroid.jobby.auth.domain.services.utils.RandomSaltFactory
import com.bravedroid.jobby.auth.domain.services.utils.SecurityUtil
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(
        private val refreshTokenRepository: RefreshTokenRepository,
        private val securityUtil: SecurityUtil,
) {
        fun createRefreshToken(user: User) :String{
            val refreshToken = securityUtil.createRandomString()
            val salt = RandomSaltFactory.create()
            val hashedRefreshToken = securityUtil.createHashedValue(refreshToken, salt)
            refreshTokenRepository.saveHashedRefreshToken(hashedRefreshToken, salt, user)
            return refreshToken
        }
}
