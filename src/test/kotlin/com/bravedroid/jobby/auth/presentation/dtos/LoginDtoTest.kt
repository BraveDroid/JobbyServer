package com.bravedroid.jobby.auth.presentation.dtos

import com.bravedroid.jobby.test.utils.ValidationHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LoginDtoTest {
    companion object {
        private val validationHelper: ValidationHelper = ValidationHelper()
    }

    private val mapObjectErrorCount = mapOf(
            LoginRequestDto() to 2,
            LoginRequestDto(email = "aaaaa") to 2,
            LoginRequestDto(email = "aaaaa.com") to 2,
            LoginRequestDto(email = "a@a") to 1,
            LoginRequestDto(email = "a@mail.com", "12341234") to 0,
            LoginRequestDto(email = "a@mail.com") to 1,
    )

    @Test
    fun `check Validation`() {
        mapObjectErrorCount.forEach { objectErrorCountPair ->
            validationHelper.validate(objectErrorCountPair.key).let {
                assertThat(it).hasSize(objectErrorCountPair.value)
            }
        }
    }
}
