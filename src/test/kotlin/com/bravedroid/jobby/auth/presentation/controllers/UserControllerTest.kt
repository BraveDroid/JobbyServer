package com.bravedroid.jobby.auth.presentation.controllers

import com.bravedroid.jobby.auth.domain.entities.User
import com.bravedroid.jobby.auth.domain.exceptions.UserNotFoundException
import com.bravedroid.jobby.auth.domain.services.UserService
import com.bravedroid.jobby.auth.presentation.dtos.UserResponseDto
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.net.URI
import javax.inject.Inject

@WebMvcTest(controllers = [UserController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
internal class UserControllerTest {

    @Inject
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userServiceMock: UserService


    @Test
    fun `check get User with incorrect format of the authorizationHeader should return UNAUTHORIZED 401`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(URI("/api/v1/user"))
                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `check get User with incorrect access token of the authorizationHeader should return NOT_FOUND 404`() {
        given(userServiceMock.findByAccessToken("eyJhbGciOiJIUzUxMiJ9")).willThrow(UserNotFoundException())
        mockMvc.perform(
            MockMvcRequestBuilders.get(URI("/api/v1/user"))
                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `check get User with correct access token of the authorizationHeader should return OK 200`() {

        given(userServiceMock.findByAccessToken("eyJhbGciOiJIUzUxMiJ9")).willReturn(
            User(
                name = "SALAH",
                email = "a@a.com",
                password = "password_hashed",
            )
        )
        mockMvc.perform(
            MockMvcRequestBuilders.get(URI("/api/v1/user"))
                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    UserResponseDto(
                        name = "SALAH",
                        email = "a@a.com",
                    ).toJsonString()
                )
            )

    }
}
