package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.ErrorMessageDto
import com.bravedroid.jobby.auth.presentation.dtos.LoginDto
import com.bravedroid.jobby.auth.presentation.dtos.RegisterDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class AuthController(
        private val userService: UserService,
) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDto): ResponseEntity<User> {
        val user = userService.save(body.toModel())
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    private fun RegisterDto.toModel() = User(
            name = name,
            email = email,
            password = password,
    )

    @PostMapping("login")
    fun login(@RequestBody body: LoginDto): ResponseEntity<Any> =
            if (userService.isUserExist(body.toDomain()))
                ResponseEntity(HttpStatus.OK)
            else
                ResponseEntity(ErrorMessageDto("User not found"), HttpStatus.NOT_FOUND)

    private fun LoginDto.toDomain(): User = User(
            name = "",
            email = email,
            password = password,
    )
}

