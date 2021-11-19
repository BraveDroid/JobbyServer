package com.bravedroid.jobby.auth.domain.services.utils

import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class DateProvider {
    fun provideExpirationDayForAccessToken(): Date =
            Date(System.currentTimeMillis() + Duration.ofDays(1).toMillis())
}
