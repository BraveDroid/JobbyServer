package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.RefreshTokenException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import com.bravedroid.jobby.auth.infrastructure.entities.RefreshTokenEntity
import com.bravedroid.jobby.auth.infrastructure.entities.toDomain
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.Instant

@Repository
class RefreshTokenRepositoryImpl(
        val refreshTokenJpaRepository: RefreshTokenJpaRepository,
        val userJpaRepository: UserJpaRepository,
) : RefreshTokenRepository {
    override fun findByHashedRefreshToken(hashedRefreshToken: String): RefreshToken? = refreshTokenJpaRepository.findByHashedToken(hashedRefreshToken)?.toDomain()
            ?: throw RefreshTokenException(hashedRefreshToken, "Unknown Refresh Token")

    override fun findUserByHashedRefreshToken(hashedRefreshToken: String): User? =
            refreshTokenJpaRepository.findByHashedToken(hashedRefreshToken)?.userEntity?.toDomain()
                    ?: throw RefreshTokenException(hashedRefreshToken, "Unknown Refresh Token")

    override fun saveHashedRefreshToken(hashedRefreshToken: String, salt: String, user: User) {
        val tokenEntity = RefreshTokenEntity(
                salt = salt,
                hashedToken = hashedRefreshToken,
                expiryDate = Instant.now().plusSeconds(Duration.ofDays(60).toSeconds()),
                userEntity = userJpaRepository.findByEmail(user.email) ?: throw UserNotFoundException()
        )
        refreshTokenJpaRepository.save(tokenEntity)
    }
}
