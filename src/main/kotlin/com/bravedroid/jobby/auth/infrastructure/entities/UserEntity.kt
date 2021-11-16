package com.bravedroid.jobby.auth.infrastructure.entities

import javax.persistence.*

@Entity
@Table(name = "USER")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,
        @Column
        val name: String,
        @Column(unique = true)
        val email: String,
        @Column
        val password: String,
)
