package com.bravedroid.jobby.auth.presentation.dtos

import com.bravedroid.jobby.auth.domain.entities.User


data class RegisterResponseDto(
    val name: String = "",
)

fun User.toRegisterResponseDto() = RegisterResponseDto(
    name = name,
)
