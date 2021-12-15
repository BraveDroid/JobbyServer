package com.bravedroid.jobby.auth.domain.services

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.BadUserPasswordException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.repositories.UserRepository
import com.bravedroid.jobby.auth.domain.services.security.SecurityService
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.CipherEncryptionService
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.EncryptionService
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.JwtDateProvider
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.JwtEncryptionService
import com.bravedroid.jobby.auth.domain.services.security.util.hashing.HashingService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class UserServiceTest {

    private lateinit var userRepositoryMock: UserRepository
    private lateinit var securityService: SecurityService
    private lateinit var securityServiceMock: SecurityService
    private lateinit var sut: UserService


    @BeforeEach
    fun setUp() {
        userRepositoryMock = mock(UserRepository::class.java)
        securityServiceMock = mock(SecurityService::class.java)
        securityService = SecurityService(
            encryptionService = EncryptionService(
                JwtEncryptionService(JwtDateProvider()),
                CipherEncryptionService(),
            ),
            hashingService = HashingService(),
        )
        sut = UserService(
            userRepositoryMock,
            securityServiceMock,
        )
    }

    @Test
    fun `verify the call of the hashPassword from the securityServiceMock in #save `() {
        `when`(securityServiceMock.hashPassword("password")).thenReturn("password_hashed")
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        sut.save(user)
        verify(userRepositoryMock).save(user.copy(password = "password_hashed"))
        verify(securityServiceMock).hashPassword(user.password)
    }

    @Test
    fun `verify the call of the findByEmail from the  userRepositoryMock in #find`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        `when`(userRepositoryMock.findByEmail(user.email)).thenReturn(user)
        `when`(securityServiceMock.comparePassword("password", user.password)).thenReturn(true)
        sut.find(user.email, user.password)
        verify(userRepositoryMock).findByEmail(user.email)
    }

    @Test
    fun `verify when findByEmail return null than throw`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        `when`(userRepositoryMock.findByEmail(user.email)).thenReturn(null)
        assertThrows<UserNotFoundException> {
            sut.find(user.email, user.password)
        }
    }

    @Test
    fun `verify when isUserPasswordCorrect return false than throw`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        `when`(userRepositoryMock.findByEmail(user.email)).thenReturn(user)
        `when`(securityServiceMock.comparePassword("password", user.password)).thenReturn(false)
        assertThrows<BadUserPasswordException> {
            sut.find(user.email, user.password)
        }
    }

    @Test
    fun `verify the call of the findById from the userRepositoryMock in #findByAccessToken`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        `when`(securityServiceMock.decryptUserIdFromJwt("Access token")).thenReturn(0)
        `when`(userRepositoryMock.findById(0)).thenReturn(user)
        sut.findByAccessToken("Access token")
        verify(userRepositoryMock).findById(0)
    }

    @Test
    fun `verify when findById return null than throw #findByAccessToken`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        `when`(securityServiceMock.decryptUserIdFromJwt("Access token")).thenReturn(0)
        `when`(userRepositoryMock.findById(0)).thenReturn(null)
        assertThrows<UserNotFoundException> {
            sut.findByAccessToken("Access token")
        }
    }

    @Test
    fun `verify the call of the findByEmail from the userRepositoryMock in #findByEmail`() {
        val user = User(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )

        `when`(userRepositoryMock.findByEmail("a@a.com")).thenReturn(user)
        sut.findByEmail(user.email)
        verify(userRepositoryMock).findByEmail(user.email)
    }
}
