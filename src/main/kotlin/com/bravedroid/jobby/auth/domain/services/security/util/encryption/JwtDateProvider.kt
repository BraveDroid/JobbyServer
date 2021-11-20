package com.bravedroid.jobby.auth.domain.services.security.util.encryption

import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtDateProvider {
    fun provideExpirationDayForAccessToken(): Date =
            Date(System.currentTimeMillis() + Duration.ofDays(1).toMillis())
}
