package ru.kheynov.data.mappers

import ru.kheynov.data.entities.RefreshToken
import ru.kheynov.domain.entities.UserDTO

fun RefreshToken.toRefreshTokenInfo(): UserDTO.RefreshTokenInfo {
    return UserDTO.RefreshTokenInfo(
        userId = this.userId,
        clientId = this.clientId,
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