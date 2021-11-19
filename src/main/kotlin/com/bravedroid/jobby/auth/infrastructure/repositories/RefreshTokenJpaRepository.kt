package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.infrastructure.entities.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByHashedToken(hashedToken: String): RefreshTokenEntity?
}
