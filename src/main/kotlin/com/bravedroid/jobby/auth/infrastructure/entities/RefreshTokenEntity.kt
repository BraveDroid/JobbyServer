package com.bravedroid.jobby.auth.infrastructure.entities

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import java.time.Instant
import javax.persistence.*

@Entity(name = "REFRESH_TOKEN")
data class RefreshTokenEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: Long = 0,

        @Column(nullable = false, unique = true)
        val hashedToken: String,

        @Column(nullable = false)
        val salt: String,

        @Column(nullable = false)
        val expiryDate: Instant,

        @OneToOne(cascade = [CascadeType.REMOVE])
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        val userEntity: UserEntity,
)

fun RefreshTokenEntity.toDomain() = RefreshToken(
        hashedToken = hashedToken,
        expiryDate = expiryDate,
        salt = salt,
)
