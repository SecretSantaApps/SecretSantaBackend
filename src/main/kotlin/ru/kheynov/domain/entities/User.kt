package ru.kheynov.domain.entities

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kheynov.security.jwt.token.RefreshToken
import java.security.AuthProvider

sealed interface UserDTO {
    @Serializable
    data class User(
        @SerialName("user_id") val userId: String,
        val username: String,
        val passwordHash: String?,
        @SerialName("auth_provider") val authProvider: String,
        val email: String,
        @SerialName("refresh_token") val refreshToken: String,
        @SerialName("refresh_token_expiration") val refreshTokenExpiration: Long,
    ) : UserDTO

    @Serializable
    data class UserEmailRegister(
        val username: String,
        val password: String,
        val email: String,
    ) : UserDTO
}