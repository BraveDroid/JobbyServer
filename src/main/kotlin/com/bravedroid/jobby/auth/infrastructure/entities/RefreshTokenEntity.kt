package com.bravedroid.jobby.auth.infrastructure.entities

import com.bravedroid.jobby.auth.domain.entities.RefreshToken
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

@Table(name = "refresh_token")
@Entity(name = "RefreshToken")
data class RefreshTokenEntity(
    @Id
    @Column(name = "refreshTokenId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val hashedToken: String,

    @Column(nullable = false)
    val salt: String,

    @Column(nullable = false)
    val expiryDate: Instant,
//    cascade = [CascadeType.ALL],
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdFk")
    var ownerUserEntity: UserEntity? = null,
) : Serializable

fun RefreshTokenEntity.toDomain(): RefreshToken = RefreshToken(
    hashedToken = hashedToken,
    expiryDate = expiryDate,
    salt = salt,
)

fun RefreshToken.toRefreshTokenEntity(): RefreshTokenEntity = RefreshTokenEntity(
    id = id,
    hashedToken = hashedToken,
    salt = salt,
    expiryDate = expiryDate,
)
