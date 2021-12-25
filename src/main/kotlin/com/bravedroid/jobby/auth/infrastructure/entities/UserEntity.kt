package com.bravedroid.jobby.auth.infrastructure.entities

import com.bravedroid.jobby.auth.domain.entities.User
import java.io.Serializable
import javax.persistence.*

@Table(name = "user")
@Entity(name = "User")
data class UserEntity(
    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column
    val name: String,

    @Column(unique = true)
    val email: String,

    @Column
    val password: String,
) : Serializable {
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "ownerUserEntity",
        cascade = [CascadeType.REMOVE],
    )
    var refreshTokenEntityList: List<RefreshTokenEntity> = emptyList()
}

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    password = password,
)

fun User.toUserEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    password = password,
)

