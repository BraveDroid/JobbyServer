package com.bravedroid.jobby.auth.domain.repositories

import com.bravedroid.jobby.auth.domain.entities.User

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: String): User?
}