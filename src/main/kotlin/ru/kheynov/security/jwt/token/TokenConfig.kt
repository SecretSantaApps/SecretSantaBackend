package ru.kheynov.security.jwt.token

import java.sql.RowIdLifetime

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val accessLifetime: Long,
    val refreshLifetime: Long,
    val secret: String,
)