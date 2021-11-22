package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.services.security.SecurityService
import org.springframework.stereotype.Service

@Service
class AccessTokenService(
    private val securityService: SecurityService,
) {
    fun createAccessToken(user: User): String = securityService.createJwt(user.id)
}
