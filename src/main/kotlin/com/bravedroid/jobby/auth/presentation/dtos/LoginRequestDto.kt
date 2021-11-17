package com.bravedroid.jobby.auth.presentation.dtos

import com.bravedroid.jobby.auth.domain.validators.PasswordConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class LoginRequestDto(
        @field:NotEmpty
        @field:Email
        val email: String = "",
        @field:PasswordConstraint
        val password: String = "",
)
