package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.exceptions.AccessTokenException
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.UserResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("user")
    fun getUser(
        @RequestHeader(name = "Authorization")
        authorizationHeader: String,
    ): ResponseEntity<Any> {
        if (!authorizationHeader.startsWith("Bearer ")) throw AccessTokenException()

        val token = authorizationHeader.substringAfter("Bearer ")
        val user = userService.findByToken(token)
        val userResponseEntity = UserResponseDto(user.name, user.email)
        return ResponseEntity.ok(userResponseEntity)
    }
}
