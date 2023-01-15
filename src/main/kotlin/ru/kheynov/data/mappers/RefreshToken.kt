package ru.kheynov.data.mappers

import ru.kheynov.data.entities.RefreshToken

fun RefreshToken.toRefreshToken(): ru.kheynov.security.jwt.token.RefreshToken {
    return ru.kheynov.security.jwt.token.RefreshToken(
        token = this.refreshToken,
        expiresAt = this.expiresAt,
    )
}

fun ru.kheynov.security.jwt.token.RefreshToken.toDataRefreshToken(
    userId: String,
    clientId: String,
): RefreshToken {
    val refreshToken = this
    return RefreshToken {
        this.userId = userId
        this.clientId = clientId
        this.refreshToken = refreshToken.token
        this.expiresAt = refreshToken.expiresAt
    }
}