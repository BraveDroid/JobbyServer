package com.bravedroid.jobby.auth.infrastructure.repositories

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import javax.inject.Inject

@DataJpaTest
@Import(UserRepositoryImpl::class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)

internal class UserRepositoryImplTest {
    @Inject
    private lateinit var sut: UserRepository

    @Inject
    private lateinit var userJpaRepository: UserJpaRepository

    @BeforeAll
    fun setUp() {
        val user = User(name = "Ahmed", email = "Ahmed@gmail.com", password = "Ahmed123123123")
        sut.save(user)
    }

    @AfterAll
    fun tearDown() {
        userJpaRepository.deleteAll()
    }

    @Test
    fun saveAndFindByEmailTest() {
        sut.findByEmail("Ahmed@gmail.com").let {
            assertThat(it).isEqualTo(User(name = "Ahmed", email = "Ahmed@gmail.com", password = "Ahmed123123123").copy(id = it!!.id   ))
            assertThat(it.id).isEqualTo(sut.findByEmail("Ahmed@gmail.com")!!.id)
        }
    }
}
