package com.bravedroid.jobby.auth.presentation.dtos

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.validators.PasswordConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class RegisterRequestDto(
        @field:NotEmpty
        val name: String = "",

        @field:NotEmpty
        @field:Email
        val email: String = "",

        @field:NotEmpty
        @field:PasswordConstraint
        val password: String = "",
)
fun RegisterRequestDto.toModel() = User(
        name = name,
        email = email,
        password = password,
)
