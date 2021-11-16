package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
        private val securityUtil: SecurityUtil,
) {
    fun save(user: User): User = userRepository.save(
            user.copy(password = hashPassword(user))
    )

    private fun hashPassword(user: User) =
            securityUtil.encode(user.password)

    fun isUserExist(user: User):Boolean = findByEmail(user.email)?.let { storedUser ->
          securityUtil.comparePassword(user.password, storedUser.password)
      } ?: false

  private  fun findByEmail(email: String): User? = userRepository.findByEmail(email)

}
