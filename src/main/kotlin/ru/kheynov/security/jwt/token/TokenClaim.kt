package ru.kheynov.security.jwt.token

data class TokenClaim(
    val name: String,
    val value: String
)