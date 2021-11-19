package com.bravedroid.jobby.auth.domain.services.utils

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Duration
import java.time.Instant
import java.util.*

internal class JwtProviderTest {
    private val dateProviderMock: DateProvider = mock(DateProvider::class.java)
    private val sut: JwtProvider = JwtProvider(dateProviderMock)

    @Test
    fun `check that two different jwt for the same user have the same result, when they are decrypted`() {
        val now = Instant.now( )
        val after4hours = now.plusSeconds(Duration.ofHours(4).toSeconds())
        val after1Hour = Instant.now().plusSeconds(Duration.ofHours(1).toSeconds())

        `when`(dateProviderMock.provideExpirationDayForAccessToken()).thenReturn(after4hours.toDateUtil())
        val jwt1 = sut.createJwt(idUser = 1)
        `when`(dateProviderMock.provideExpirationDayForAccessToken()).thenReturn(Date.from(after1Hour))
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
        val before4hours = now.minusSeconds(Duration.ofHours(4).toSeconds())
        `when`(dateProviderMock.provideExpirationDayForAccessToken()).thenReturn(before4hours.toDateUtil())

        val jwt1 = sut.createJwt(idUser = 1)
        assertThrows<ExpiredJwtException> {
             sut.decryptUserIdFromJwt(jwt1)
        }
    }

    @Test
    fun `check that decrypting a jwt that was expired should throw SignatureException`(){
        val now = Instant.now( )
        val after4hours = now.plusSeconds(Duration.ofHours(4).toSeconds())
        `when`(dateProviderMock.provideExpirationDayForAccessToken()).thenReturn(after4hours.toDateUtil())

        val jwt1 = sut.createJwt(idUser = 1).plus("LOL")

        assertThrows<SignatureException> {
             sut.decryptUserIdFromJwt(jwt1)
        }
    }
}
