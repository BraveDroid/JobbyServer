package com.bravedroid.jobby.auth.presentation.dtos

import javax.validation.constraints.NotEmpty

data class RefreshTokenRequestDto(
    @field:NotEmpty
    val refreshToken: String,
)
