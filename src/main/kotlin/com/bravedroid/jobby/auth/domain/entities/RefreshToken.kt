package com.bravedroid.jobby.auth.domain.entities

import java.time.Instant

data class RefreshToken(
        val token: String,
        val expiryDate: Instant,
)
