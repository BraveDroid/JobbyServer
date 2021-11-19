package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.services.utils.SecurityUtil
import org.springframework.stereotype.Service

@Service
class AccessTokenService(
        private val securityUtil: SecurityUtil,
) {
    fun createAccessToken(user: User): String = securityUtil.createJwt(user.id)
}
