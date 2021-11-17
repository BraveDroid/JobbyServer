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
    override fun findByToken(refreshToken: String): RefreshToken? = refreshTokenJpaRepository.findByToken(refreshToken)?.toDomain()
            ?: throw RefreshTokenException(refreshToken, "Unknown Refresh Token")

    override fun findUserByToken(refreshToken: String): User? =
            refreshTokenJpaRepository.findByToken(refreshToken)?.userEntity?.toDomain()
                    ?: throw RefreshTokenException(refreshToken, "Unknown Refresh Token")

    override fun saveRefreshToken(createRefreshToken: String, user: User) {
        val tokenEntity = RefreshTokenEntity(
                token = createRefreshToken,
                expiryDate = Instant.now().plusSeconds(Duration.ofDays(60).toSeconds()),
                userEntity = userJpaRepository.findByEmail(user.email) ?: throw UserNotFoundException()
        )
        refreshTokenJpaRepository.save(tokenEntity)
    }
}
