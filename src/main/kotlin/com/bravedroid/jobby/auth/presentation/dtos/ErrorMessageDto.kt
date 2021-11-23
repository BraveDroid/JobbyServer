package com.bravedroid.jobby.auth.presentation.dtos

data class ErrorMessageDto(
    val errorMessage: String,
)

fun Exception.toErrorMessageDto() =
    ErrorMessageDto(localizedMessage)

data class FieldErrorMessageDto(
    val field: String,
    val errorMessage: String,
)
