package com.bravedroid.jobby.auth.presentation.dtos

data class RefreshTokenResponseDto(
    val accessToken: String,
) {
    val tokenType: String = "Bearer"
}
