package com.bravedroid.jobby.auth.domain.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityUtil {
    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun encode(password: String): String = passwordEncoder.encode(password)

    fun comparePassword(hashedPassword1: String, hashedPassword2: String): Boolean =
            passwordEncoder.matches(hashedPassword1, hashedPassword2)
}