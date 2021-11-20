package com.bravedroid.jobby.auth.domain.services.security

import com.bravedroid.jobby.auth.domain.exceptions.AccessTokenException
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.JwtDateProvider
import com.bravedroid.jobby.auth.domain.services.security.util.encryption.JwtEncryptionService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Duration
import java.time.Instant
import java.util.*

internal class JwtProviderTest {
    private val jwtDateProviderMock: JwtDateProvider = mock(JwtDateProvider::class.java)
    private val sut: JwtEncryptionService = JwtEncryptionService(jwtDateProviderMock)

    @Test
    fun `check that two different jwt for the same user have the same result, when they are decrypted`() {
        val now = Instant.now( )
        val after4hours = now.plusSeconds(Duration.ofHours(4).seconds)
        val after1Hour = Instant.now().plusSeconds(Duration.ofHours(1).seconds)

        `when`(jwtDateProviderMock.provideExpirationDayForAccessToken()).thenReturn(after4hours.toDateUtil())
        val jwt1 = sut.createJwt(idUser = 1)
        `when`(jwtDateProviderMock.provideExpirationDayForAccessToken()).thenReturn(Date.from(after1Hour))
        val jwt2 = sut.createJwt(idUser = 1)

        assertThat(jwt1).isNotEqualTo(jwt2)

        val userId1 = sut.decryptUserIdFromJwt(jwt1)
        val userId2 = sut.decryptUserIdFromJwt(jwt2)

        assertThat(userId1).isEqualTo(userId2)
    }

   private fun Instant.toDateUtil(): Date = Date.from(this)


    @Test
    fun `check that decrypting a jwt that was expired should throw ExpiredJwtException`(){
        val now = Instant.now( )
        val before4hours = now.minusSeconds(Duration.ofHours(4).seconds)
        `when`(jwtDateProviderMock.provideExpirationDayForAccessToken()).thenReturn(before4hours.toDateUtil())

        val jwt1 = sut.createJwt(idUser = 1)
        assertThrows<AccessTokenException> {
             sut.decryptUserIdFromJwt(jwt1)
        }
    }

    @Test
    fun `check that decrypting a jwt that was expired should throw SignatureException`(){
        val now = Instant.now( )
        val after4hours = now.plusSeconds(Duration.ofHours(4).seconds)
        `when`(jwtDateProviderMock.provideExpirationDayForAccessToken()).thenReturn(after4hours.toDateUtil())

        val jwt1 = sut.createJwt(idUser = 1).plus("LOL")

        assertThrows<AccessTokenException> {
             sut.decryptUserIdFromJwt(jwt1)
        }
    }
}
