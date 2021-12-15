package com.bravedroid.jobby.auth.infrastructure.entities

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import java.time.Instant
import javax.persistence.*

@Entity(name = "REFRESH_TOKEN")
data class RefreshTokenEntity(
    @Id
    @Column(name = "RefreshTokenId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val hashedToken: String,

    @Column(nullable = false)
    val salt: String,

    @Column(nullable = false)
    val expiryDate: Instant,

    @ManyToOne
    @JoinColumn(name = "userIdFk")
    val userEntity: UserEntity,
)

fun RefreshTokenEntity.toDomain() = RefreshToken(
    hashedToken = hashedToken,
    expiryDate = expiryDate,
    salt = salt,
)
