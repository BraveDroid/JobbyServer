package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.services.RefreshTokenService
import com.bravedroid.jobby.auth.domain.services.SecurityUtil
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.xml.bind.ValidationException

@RestController
@RequestMapping("api/v1")
class AuthController(
        private val userService: UserService,
        private val securityUtil: SecurityUtil,
        private val refreshTokenService: RefreshTokenService,
) {

    @PostMapping("register")
    //todo 1 add @Valid
    fun register(@RequestBody body: RegisterDto): ResponseEntity<User> {
        with(body) {
            listOf(email, name, password).any {
                it.isEmpty() || it.isBlank()
            }.let { isOneInputEmptyOrBlank ->
                if (isOneInputEmptyOrBlank) throw ValidationException("Inputs should not be empty!")
            }
        }
        val user = userService.save(body.toModel())
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    private fun RegisterDto.toModel() = User(
            name = name,
            email = email,
            password = password,
    )

    @PostMapping("login")
    fun login(@Valid @RequestBody body: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val user = userService.findUser(body.email, body.password)
        val jwt = securityUtil.createJwt(user.id)
        val refreshToken = refreshTokenService.createRefreshTokenForUser(user)

        return ResponseEntity(
                LoginResponseDto(
                        accessToken = jwt,
                        refreshToken = refreshToken,
                ),
                HttpStatus.OK)
    }

    @GetMapping("user")
    fun getUser(
            @RequestHeader(name = "Authorization")
            authorizationHeader: String,
    ): ResponseEntity<Any> {
        val token = authorizationHeader.substringAfter("Bearer ")
        return ResponseEntity.ok(securityUtil.decryptUserIdFromJwt(token))
    }

    //to be deleted when todo 1 is DONE
    @ExceptionHandler(ValidationException::class)
    fun handleException(e: ValidationException): ResponseEntity<ErrorMessageDto> {
        return ResponseEntity(e.toErrorMessageDto(), HttpStatus.BAD_REQUEST)
    }
}

