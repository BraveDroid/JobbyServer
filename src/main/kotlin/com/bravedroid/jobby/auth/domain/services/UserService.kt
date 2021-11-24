package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.BadUserPasswordException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import com.bravedroid.jobby.auth.domain.services.security.SecurityService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val securityService: SecurityService,
) {
    fun save(user: User): User = userRepository.save(
        user.copy(password = hashPassword(user))
    )

    private fun hashPassword(user: User) = securityService.hashPassword(user.password)

    fun find(email: String, password: String): User {
        val storedUser: User = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        if (!isUserPasswordCorrect(password, storedUser.password)) throw BadUserPasswordException()
        return storedUser
    }

    private fun isUserPasswordCorrect(password: String, storedPassword: String): Boolean =
        securityService.comparePassword(password, storedPassword)

    fun findByAccessToken(accessToken: String): User {
        val id = securityService.decryptUserIdFromJwt(accessToken)
        return userRepository.findById(id)?: throw UserNotFoundException()
    }

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)
}
