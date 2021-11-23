package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.services.AccessTokenService
import com.bravedroid.jobby.auth.domain.services.RefreshTokenService
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.RegisterRequestDto
import com.bravedroid.jobby.auth.presentation.dtos.RegisterResponseDto
import com.bravedroid.jobby.auth.presentation.dtos.toModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI
import javax.inject.Inject

@WebMvcTest(controllers = [AuthController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters

internal class AuthControllerTest {
    @Inject
    private lateinit var mockMvc: MockMvc

    @Inject
    private lateinit var jsonRequest: JacksonTester<RegisterRequestDto>

    @MockBean
    private lateinit var userServiceMock: UserService

    @MockBean
    private lateinit var accessTokenServiceMock: AccessTokenService

    @MockBean
    private lateinit var refreshTokenServiceMock: RefreshTokenService


    @Test
    fun `check register with non valid body should return badRequest 400`() {
        val list = listOf(
            RegisterRequestDto(),
            RegisterRequestDto(
                name = "SALAH",
                email = "@",
                password = "password",
            ),
            RegisterRequestDto(
                name = "SALAH",
                email = "@",
                password = "pass",
            ),
            RegisterRequestDto(
                name = "SALAH",
                email = "a@a.com",
                password = "pass",
            ),
        )
        list.forEach {
            mockMvc.perform(
                post(URI("/api/v1/register"))
                    .content(jsonRequest.write(it).json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }
    }

    @Test
    fun `check register with valid body should return created 201`() {
        val requestObject = RegisterRequestDto(
            name = "SALAH",
            email = "a@a.com",
            password = "password",
        )
        given(userServiceMock.save(requestObject.toModel())).willReturn(
            User(
                0, name = "SALAH",
                email = "a@a.com",
                password = "password_hashed",
            )
        )

        mockMvc.perform(
            post(URI("/api/v1/register"))
                .content(jsonRequest.write(requestObject).json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().json(RegisterResponseDto("SALAH").toJsonString()))
    }
}

private fun RegisterResponseDto.toJsonString(): String {
    val objectMapper = ObjectMapper()
    return objectMapper.writeValueAsString(this)
}

