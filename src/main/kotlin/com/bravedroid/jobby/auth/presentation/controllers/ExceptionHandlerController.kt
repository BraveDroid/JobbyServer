package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.exceptions.BadUserPasswordException
import com.bravedroid.jobby.auth.domain.exceptions.RefreshTokenException
import com.bravedroid.jobby.auth.domain.exceptions.UserEmailNotAvailableException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.presentation.dtos.ErrorMessageDto
import com.bravedroid.jobby.auth.presentation.dtos.FieldErrorMessageDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Any =
            when (e) {
                is MethodArgumentNotValidException -> {
                    val fieldErrorMessageDtoList = e.bindingResult.fieldErrors.map {
                        FieldErrorMessageDto(it.field, it.defaultMessage ?: "")
                    }
                    ResponseEntity(fieldErrorMessageDtoList, HttpStatus.BAD_REQUEST)
                }
                is UserEmailNotAvailableException -> {
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.BAD_REQUEST)
                }
                is UserNotFoundException -> {
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.NOT_FOUND)
                }
                is BadUserPasswordException -> {
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.BAD_REQUEST)
                }
                is RefreshTokenException->{
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.FORBIDDEN)
                }
                else -> ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.INTERNAL_SERVER_ERROR)
            }
}
