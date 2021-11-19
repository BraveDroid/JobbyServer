package com.bravedroid.jobby.auth.domain.repositories

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import com.bravedroid.jobby.auth.domain.entities.User

interface RefreshTokenRepository {
    fun findByHashedRefreshToken(hashedRefreshToken: String): RefreshToken?
    fun findUserByHashedRefreshToken(hashedRefreshToken: String): User?
    fun saveHashedRefreshToken(hashedRefreshToken: String, salt: String, user: User)
}
