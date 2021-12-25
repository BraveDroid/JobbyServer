package com.bravedroid.jobby.auth.domain.entities

import java.time.Instant

data class RefreshToken(
    val id: Long = 0,
    val hashedToken: String,
    val expiryDate: Instant,
    val salt: String,
)
