package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.RefreshTokenRepository
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import com.bravedroid.jobby.auth.infrastructure.entities.toRefreshTokenEntity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.inject.Inject

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(
    value = [
        RefreshTokenRepositoryImpl::class,
        UserRepositoryImpl::class,
    ]
)
internal class RefreshTokenRepositoryImplTest {
    @Inject
    private lateinit var sut: RefreshTokenRepository

    @Inject
    private lateinit var refreshTokenJpaRepository: RefreshTokenJpaRepository

    @Inject
    private lateinit var userRepository: UserRepository

    @Inject
    private lateinit var userJpaRepository: UserJpaRepository

    private val user1 = User(name = "Ahmed", email = "Ahmed@gmail.com", password = "Ahmed123123123")
    private val user2 = User(name = "Mohsen", email = "Mohsen@gmail.com", password = "Mohsen123123123")

    @BeforeEach
    fun setUp() {
        userRepository.save(user1)
        userRepository.save(user2)
    }

    @AfterEach
    fun tearDown() {
        refreshTokenJpaRepository.deleteAll()
        userJpaRepository.deleteAll()
    }

    @Test
    fun saveHashedRefreshTokenTest() {
        sut.saveHashedRefreshToken(
            hashedRefreshToken = "hashedRefreshToken1",
            salt = "salt1",
            user = user1,
        )

        val user = sut.findUserByHashedRefreshToken("hashedRefreshToken1").also {
            Assertions.assertThat(it).isEqualTo(user1.copy(id = it!!.id))
        }!!

        sut.findByUser(user).let {
            Assertions.assertThat(it!!.salt).isEqualTo("salt1")
            Assertions.assertThat(it.hashedToken).isEqualTo("hashedRefreshToken1")
        }
        sut.findAllRefreshTokenByUser(user).let {
            Assertions.assertThat(it).hasSize(1)
        }
    }

    @Test
    fun cascadingDeleteTest() {
        sut.saveHashedRefreshToken(
            hashedRefreshToken = "hashedRefreshToken21",
            salt = "salt21",
            user = user2,
        )
        sut.saveHashedRefreshToken(
            hashedRefreshToken = "hashedRefreshToken22",
            salt = "salt22",
            user = user2,
        )

        val user21 = sut.findUserByHashedRefreshToken("hashedRefreshToken21")!!
        val user22 = sut.findUserByHashedRefreshToken("hashedRefreshToken22")!!

        Assertions.assertThat(user21).isEqualTo(user22)

        val refreshToken = sut.findAllRefreshTokenByUser(user21)
        Assertions.assertThat(refreshToken).hasSize(2)

        refreshTokenJpaRepository.count().let {
            Assertions.assertThat(it).isEqualTo(2)
        }

        refreshTokenJpaRepository.delete(refreshToken.first().toRefreshTokenEntity())

        refreshTokenJpaRepository.count().let {
            Assertions.assertThat(it).isEqualTo(2)
        }

        userJpaRepository.deleteByEmail(user21.email)


        refreshTokenJpaRepository.count().let {
            Assertions.assertThat(it).isEqualTo(0)
        }
        userJpaRepository.count().let {
            Assertions.assertThat(it).isEqualTo(1)
        }
    }
}
