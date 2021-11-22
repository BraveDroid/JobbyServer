package com.bravedroid.jobby.auth.domain.services.security.util.encryption

import org.springframework.stereotype.Service

@Service
class EncryptionService(
    private val jwtEncryptionService: JwtEncryptionService,
    private val cipherEncryptionService: CipherEncryptionService,
) {
    fun encryptCypherValue(data: String) = cipherEncryptionService.encryptValue(data)
    fun decryptCypherValue(data: String) = cipherEncryptionService.decryptValue(data)
    fun createJwtValue(idUser: Long): String = jwtEncryptionService.createJwt(idUser)
    fun decryptJwtValue(jwt: String): Long = jwtEncryptionService.decryptUserIdFromJwt(jwt)
}
