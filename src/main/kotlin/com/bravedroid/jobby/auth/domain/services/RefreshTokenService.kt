package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(
        private val refreshTokenRepository: RefreshTokenRepository,
        private val securityUtil: SecurityUtil,
) {
        fun createRefreshTokenForUser(user: User) :String{
            val createRefreshToken = securityUtil.createRefreshToken()
            refreshTokenRepository.saveRefreshToken(createRefreshToken, user)
            return createRefreshToken
        }
}
