package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.UserEmailNotAvailableException
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import com.bravedroid.jobby.auth.infrastructure.entities.UserEntity
import com.bravedroid.jobby.auth.infrastructure.entities.toDomain
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
        private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun save(user: User): User {
        val emailAlreadyExist = findByEmail(user.email) != null
        if (emailAlreadyExist) throw UserEmailNotAvailableException(user.email)

        return userJpaRepository.save(user.toPersistenceEntity()).toDomain()
    }

    override fun findByEmail(email: String) = userJpaRepository.findByEmail(email)?.toDomain()


    private fun User.toPersistenceEntity(): UserEntity = UserEntity(
            name = name,
            email = email,
            password = password,
    )

}

