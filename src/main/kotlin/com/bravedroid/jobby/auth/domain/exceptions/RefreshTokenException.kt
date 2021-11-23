package com.bravedroid.jobby.auth.domain.exceptions

class RefreshTokenException(
    token: String,
    message: String,
) : RuntimeException("Failed for [$token]: $message")
