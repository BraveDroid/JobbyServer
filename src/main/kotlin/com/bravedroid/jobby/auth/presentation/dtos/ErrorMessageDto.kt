package com.bravedroid.jobby.auth.presentation.dtos

import javax.xml.bind.ValidationException

data class ErrorMessageDto(
        val errorMessage: String,
)

fun ValidationException.toErrorMessageDto() =
        ErrorMessageDto(this.message ?: "UnKnown message")

data class FieldErrorMessageDto(
        val field: String,
        val errorMessage: String,
)
