package com.bravedroid.jobby.auth.domain.repositories

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import com.bravedroid.jobby.auth.domain.entities.User

interface RefreshTokenRepository {
    fun findByToken(refreshToken: String): RefreshToken?
    fun findUserByToken(refreshToken: String): User?
    fun saveRefreshToken(createRefreshToken: String, user: User)
}
