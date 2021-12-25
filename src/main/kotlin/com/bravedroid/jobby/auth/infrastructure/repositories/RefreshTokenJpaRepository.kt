package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.infrastructure.entities.RefreshTokenEntity
import com.bravedroid.jobby.auth.infrastructure.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional
interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByHashedToken(hashedToken: String): RefreshTokenEntity?
    fun findByOwnerUserEntity(ownerUserEntity: UserEntity): RefreshTokenEntity?
    fun findAllByOwnerUserEntity(ownerUserEntity: UserEntity): List<RefreshTokenEntity>?
}
