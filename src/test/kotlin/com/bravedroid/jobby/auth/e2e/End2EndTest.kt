package com.bravedroid.jobby.auth.e2e

import com.bravedroid.jobby.auth.domain.services.security.SecurityService
import com.bravedroid.jobby.auth.presentation.dtos.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import javax.inject.Inject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class End2EndTest {
    @Inject
    private lateinit var testRestTemplate: TestRestTemplate

    @Inject
    private lateinit var securityService: SecurityService

    @LocalServerPort
    private val serverPort = 0

    @Test
    fun `test Register then Login then access userResource`() {
        val name = "Mouhamed"
        val email = "Mouhamed1${securityService.createRandomString()}@gmail.com"
        val password = "zigzagNouNou"

        with(testRestTemplate) {
            checkRegister(name, email, password)
            val accessToken = checkLogin(email, password)
            checkAccessUserResource(name, email, accessToken)
        }
    }

    private fun TestRestTemplate.checkRegister(
        name: String,
        email: String,
        password: String,
    ) {
        val registerRequestDto = RegisterRequestDto(
            name = name,
            email = email,
            password = password,
        )

        val registerUrl = "http://localhost:$serverPort/api/v1/register"
        val registerRequest = HttpEntity(registerRequestDto)
        val registerResponseEntity = postForEntity(registerUrl, registerRequest, RegisterResponseDto::class.java)
        Assertions.assertThat(registerResponseEntity.body).isEqualTo(RegisterResponseDto(name))
        Assertions.assertThat(registerResponseEntity.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    private fun TestRestTemplate.checkLogin(
        email: String,
        password: String,
    ): String {
        val loginUrl = "http://localhost:$serverPort/api/v1/login"
        val loginRequestDto = LoginRequestDto(
            email = email,
            password = password,
        )
        val loginRequest = HttpEntity(loginRequestDto)

        val loginResponseEntity = postForEntity(loginUrl, loginRequest, LoginResponseDto::class.java)
        Assertions.assertThat(loginResponseEntity.body).isInstanceOf(LoginResponseDto::class.java)
        Assertions.assertThat(loginResponseEntity.statusCode).isEqualTo(HttpStatus.OK)

        return loginResponseEntity.body!!.accessToken
    }

    private fun TestRestTemplate.checkAccessUserResource(
        name: String,
        email: String,
        accessToken: String,
    ) {
        val userResourceUrl = "http://localhost:$serverPort/api/v1/user"

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }
        val httpEntity = HttpEntity<Any>(headers)
        val userResponseEntity: ResponseEntity<UserResponseDto> =
            exchange(userResourceUrl, HttpMethod.GET, httpEntity, UserResponseDto::class.java)
        Assertions.assertThat(userResponseEntity.body).isEqualTo(
            UserResponseDto(
                name = name,
                email = email,
            )
        )
        Assertions.assertThat(userResponseEntity.statusCode).isEqualTo(HttpStatus.OK)
    }
}
