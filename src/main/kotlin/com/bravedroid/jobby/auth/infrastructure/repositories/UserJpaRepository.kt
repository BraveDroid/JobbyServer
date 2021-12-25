package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.infrastructure.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

@Transactional
interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun deleteByEmail(email: String): Long
}
