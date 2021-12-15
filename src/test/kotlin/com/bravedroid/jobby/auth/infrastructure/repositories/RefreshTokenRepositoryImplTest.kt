package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import javax.inject.Inject

@DataJpaTest
@Import(value = [
    RefreshTokenRepositoryImpl::class,
    UserRepositoryImpl::class,
])
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
internal class RefreshTokenRepositoryImplTest {
    @Inject
    private lateinit var sut: RefreshTokenRepository

    @Inject
    private lateinit var refreshTokenJpaRepository: RefreshTokenJpaRepository

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var userJpaRepository: UserJpaRepository

    private val user = User(name = "Ahmed", email = "Ahmed@gmail.com", password = "Ahmed123123123")

    @BeforeEach
    fun setUp() {
        userRepository.save(user)
    }

    @AfterEach
    fun tearDown() {
        userJpaRepository.deleteAll()
        refreshTokenJpaRepository.deleteAll()
    }

    @Test
    fun saveHashedRefreshTokenTest() {
        sut.saveHashedRefreshToken(
            hashedRefreshToken = "hashedRefreshToken",
            salt = "salt",
            user = user,
        )

        val user=sut.findUserByHashedRefreshToken("hashedRefreshToken").also {
            Assertions.assertThat(it).isEqualTo(user.copy(id = it!!.id))
        }!!

        sut.findByUser(user).let {
            Assertions.assertThat(it!!.salt).isEqualTo("salt")
            Assertions.assertThat(it.hashedToken).isEqualTo("hashedRefreshToken")
        }
        sut.findAllRefreshTokenByUser(user).let {
            Assertions.assertThat(it).hasSize(1)
        }
    }
}
