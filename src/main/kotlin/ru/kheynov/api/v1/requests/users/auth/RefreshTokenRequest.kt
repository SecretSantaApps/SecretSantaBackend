package ru.kheynov.api.v1.requests.users.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val oldRefreshToken: String,
)
