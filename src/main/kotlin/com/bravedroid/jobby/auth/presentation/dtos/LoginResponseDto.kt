package com.bravedroid.jobby.auth.presentation.dtos

data class LoginResponseDto(
        val accessToken: String,
        val refreshToken: String,
){
    val tokenType: String = "Bearer"
}

