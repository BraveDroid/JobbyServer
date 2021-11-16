package com.bravedroid.jobby.auth.presentation.dtos


data class RegisterDto(
        val name: String = "",
        val email: String = "",
        val password: String = "",
)

