package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.infrastructure.entities.RefreshTokenEntity
import com.bravedroid.jobby.auth.infrastructure.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByHashedToken(hashedToken: String): RefreshTokenEntity?
    fun findByUserEntity(userEntity: UserEntity): RefreshTokenEntity?
    fun findAllByUserEntity(userEntity: UserEntity): List<RefreshTokenEntity>?
}
