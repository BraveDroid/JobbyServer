package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.exceptions.*
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
                is AccessTokenException ->{
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.UNAUTHORIZED)
                }
                is RefreshTokenException->{
                    ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.FORBIDDEN)
                }
                else -> ResponseEntity(ErrorMessageDto(e.localizedMessage), HttpStatus.INTERNAL_SERVER_ERROR)
            }
}
//
//fun handleException(e: Exception): Any {
//    X.from(e).handle()
//}
//
//enum class X(exception: KClass<out Exception>, action: (exception: Exception) -> ResponseEntity<Any>) {
//    METHOD_ARGUMENT_NOT_VALID(MethodArgumentNotValidException::class, {e ->
//        e as MethodArgumentNotValidException
//        val fieldErrorMessageDtoList = e.bindingResult.fieldErrors.map {
//            com.bravedroid.jobby.auth.presentation.dtos.FieldErrorMessageDto(it.field, it.defaultMessage ?: "")
//        }
//        org.springframework.http.ResponseEntity(fieldErrorMessageDtoList, org.springframework.http.HttpStatus.BAD_REQUEST)
//    }),
//
//    USER_EMAIL_NOT_AVAILABLE(UserEmailNotAvailableException::class),
//    USER_NOT_FOUND(UserNotFoundException::class),
//    BAD_USER_PASSWORD(BadUserPasswordException::class),
//    ACCESS_TOKEN(AccessTokenException::class),
//    REFRESH_TOKEN(RefreshTokenException::class),
//    OTHER()
//}
