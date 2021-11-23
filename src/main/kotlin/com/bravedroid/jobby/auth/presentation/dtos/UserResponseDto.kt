package com.bravedroid.jobby.auth.presentation.dtos

import com.bravedroid.jobby.auth.domain.entities.User

data class UserResponseDto(
    val name: String,
    val email: String,
)

fun User.toUserResponseDto(): UserResponseDto = UserResponseDto(
    name = name,
    email = email
)
