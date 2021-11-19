package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.services.AccessTokenService
import com.bravedroid.jobby.auth.domain.services.RefreshTokenService
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1")
class AuthController(
        private val userService: UserService,
        private val accessTokenService: AccessTokenService,
        private val refreshTokenService: RefreshTokenService,
) {

    @PostMapping("register")
    fun register(@Valid @RequestBody body: RegisterRequestDto): ResponseEntity<RegisterResponseDto> {
        val user = userService.save(body.toModel())
        return ResponseEntity(user.toRegisterResponseDto(), HttpStatus.CREATED)
    }

    @PostMapping("login")
    fun login(@Valid @RequestBody body: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val user = userService.find(body.email, body.password)
        val accessToken = accessTokenService.createAccessToken(user)
        val refreshToken = refreshTokenService.createRefreshToken(user)

        return ResponseEntity(
                LoginResponseDto(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                ),
                HttpStatus.OK,
        )
    }
}

