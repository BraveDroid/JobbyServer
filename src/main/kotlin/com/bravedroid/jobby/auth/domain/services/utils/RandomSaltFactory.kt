package com.bravedroid.jobby.auth.domain.services.utils

import java.security.SecureRandom
import java.util.*

object RandomSaltFactory {
    fun create(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }
}
