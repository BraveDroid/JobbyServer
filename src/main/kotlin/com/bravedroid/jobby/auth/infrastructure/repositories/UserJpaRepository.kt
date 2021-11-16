package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.infrastructure.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Int> {
    fun findByEmail(email: String): UserEntity?
}
