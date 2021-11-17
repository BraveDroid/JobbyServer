package com.bravedroid.jobby.auth.infrastructure.entities

import com.bravedroid.jobby.auth.domain.entities.User
import javax.persistence.*

@Entity
@Table(name = "USER")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column
        val name: String,

        @Column(unique = true)
        val email: String,

        @Column
        val password: String,
)
fun UserEntity.toDomain() = User(
        id = id,
        name = name,
        email = email,
        password = password,
)
