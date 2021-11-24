package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.BadUserPasswordException
import com.bravedroid.jobby.auth.domain.exceptions.RefreshTokenException
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.services.AccessTokenService
import com.bravedroid.jobby.auth.domain.services.RefreshTokenService
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.*
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
    private lateinit var jsonRequestForRegisterRequest: JacksonTester<RegisterRequestDto>

    @Inject
    private lateinit var jsonRequestForLoginRequest: JacksonTester<LoginRequestDto>

    @Inject
    private lateinit var jsonRequestForRefreshTokenRequest: JacksonTester<RefreshTokenRequestDto>

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
                    .content(jsonRequestForRegisterRequest.write(it).json)
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
                id = 0,
                name = "SALAH",
                email = "a@a.com",
                password = "password_hashed",
            )
        )

        mockMvc.perform(
            post(URI("/api/v1/register"))
                .content(jsonRequestForRegisterRequest.write(requestObject).json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().json(RegisterResponseDto("SALAH").toJsonString()))
    }

    @Test
    fun `check login with non valid body should return NOT_FOUND 404`() {
        val list = listOf(
            LoginRequestDto(
                email = "a@a.com",
                password = "password",
            ),
        )

        given(userServiceMock.find("a@a.com", "password")).willThrow(
            UserNotFoundException()
        )

        list.forEach {
            mockMvc.perform(
                post(URI("/api/v1/login"))
                    .content(jsonRequestForLoginRequest.write(it).json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound)
        }
    }

    @Test
    fun `check login with non valid body should return BAD_REQUEST 400`() {
        val list = listOf(
            LoginRequestDto(
                email = "a@a.com",
                password = "password",
            ),
        )

        given(userServiceMock.find("a@a.com", "password")).willThrow(
            BadUserPasswordException()
        )

        list.forEach {
            mockMvc.perform(
                post(URI("/api/v1/login"))
                    .content(jsonRequestForLoginRequest.write(it).json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }
    }

    @Test
    fun `check login with valid body should return OK 200`() {
        val list = listOf(
            LoginRequestDto(
                email = "a@a.com",
                password = "password",
            ),
        )

        val user = User(
            0, name = "SALAH",
            email = "a@a.com",
            password = "password_hashed",
        )
        given(userServiceMock.find("a@a.com", "password")).willReturn(user)
        given(accessTokenServiceMock.createAccessToken(user)).willReturn("eyJhbGciOiJIUzUxMiJ9")
        given(refreshTokenServiceMock.createRefreshToken(user)).willReturn("j1teIkkc0YbXhJhpiSHSJDM")

        list.forEach {
            mockMvc.perform(
                post(URI("/api/v1/login"))
                    .content(jsonRequestForLoginRequest.write(it).json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
                .andExpect(
                    content().json(
                        LoginResponseDto(
                            "eyJhbGciOiJIUzUxMiJ9",
                            "j1teIkkc0YbXhJhpiSHSJDM",
                        ).toJsonString()
                    )
                )
        }
    }

    @Test
    fun `check refreshToken with invalid body should return FORBIDDEN 403`() {
        val dto = RefreshTokenRequestDto("j1teIkkc0YbXhJhpiSHSJDM")
        val user = User(
            0, name = "SALAH",
            email = "a@a.com",
            password = "password_hashed",
        )
        given(refreshTokenServiceMock.isValidateRefreshTokenFormat("j1teIkkc0YbXhJhpiSHSJDM")).willReturn(
            false
        )
        given(refreshTokenServiceMock.getEmailFromRefreshToken("j1teIkkc0YbXhJhpiSHSJDM")).willThrow(
            RefreshTokenException("j1teIkkc0YbXhJhpiSHSJDM", "Unknown User")
        )
        given(refreshTokenServiceMock.isRefreshTokenExist(user, "j1teIkkc0YbXhJhpiSHSJDM")).willReturn(false)
        mockMvc.perform(
            post(URI("/api/v1/refresh-token"))
                .content(jsonRequestForRefreshTokenRequest.write(dto).json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden)
    }

    @Test
    fun `check refreshToken with valid content body should return OK 200`() {
        val dto = RefreshTokenRequestDto("j1teIkkc0YbXhJhpiSHSJDM")
        val user = User(
            0, name = "SALAH",
            email = "a@a.com",
            password = "password_hashed",
        )
        given(refreshTokenServiceMock.isValidateRefreshTokenFormat("j1teIkkc0YbXhJhpiSHSJDM")).willReturn(true)
        given(refreshTokenServiceMock.getEmailFromRefreshToken("j1teIkkc0YbXhJhpiSHSJDM")).willReturn("a@a.com")
        given(userServiceMock.findByEmail("a@a.com")).willReturn(user)
        given(refreshTokenServiceMock.isRefreshTokenExist(user, "j1teIkkc0YbXhJhpiSHSJDM")).willReturn(true)
        given(accessTokenServiceMock.createAccessToken(user)).willReturn("eyJhbGciOiJIUzUxMiJ9")

        mockMvc.perform(
            post(URI("/api/v1/refresh-token"))
                .content(jsonRequestForRefreshTokenRequest.write(dto).json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    RefreshTokenResponseDto(
                        "eyJhbGciOiJIUzUxMiJ9",
                    ).toJsonString()
                )
            )
    }
}

fun Any.toJsonString(): String {
    val objectMapper = ObjectMapper()
    return objectMapper.writeValueAsString(this)
}
