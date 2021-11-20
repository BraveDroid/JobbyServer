package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.BadUserPasswordException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import com.bravedroid.jobby.auth.domain.services.utils.SecurityUtil
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
        private val securityUtil: SecurityUtil,
) {
    fun save(user: User): User = userRepository.save(
            user.copy(password = hashPassword(user))
    )

    private fun hashPassword(user: User) = securityUtil.hashPassword(user.password)

    fun find(email: String, password: String): User {
        val storedUser: User = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        if (!isUserPasswordCorrect(password, storedUser.password)) throw BadUserPasswordException()
        return storedUser
    }

    private fun isUserPasswordCorrect(password: String, storedPassword: String): Boolean =
            securityUtil.comparePassword(password, storedPassword)

    fun findUserByToken(token: String): User {
        val id  = securityUtil.decryptUserIdFromJwt(token)
        return userRepository.findById(id)
    }
}
