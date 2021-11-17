package com.bravedroid.jobby

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun test() {
        val authorizationHeader = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIzIiwiZXhwIjoxNjM3MTg2NzY2fQ.o892xN4RJy9bN1pKtMi_eBiEY93jKWz1GPjsEQ5QLnRS4zm-43RgOmMsp7iakHylpRcYK5Cimj8CYpuk04g7yw"
        val token = authorizationHeader.substringAfter("Bearer ")
        assertThat(token).isEqualTo("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIzIiwiZXhwIjoxNjM3MTg2NzY2fQ.o892xN4RJy9bN1pKtMi_eBiEY93jKWz1GPjsEQ5QLnRS4zm-43RgOmMsp7iakHylpRcYK5Cimj8CYpuk04g7yw")
    }
}
