package ru.kheynov.security.jwt.token

data class TokenPair(
    val accessToken: String,
    val refreshToken: RefreshToken,
)

data class RefreshToken(
    val token: String,
    val expiresAt: Long,
)