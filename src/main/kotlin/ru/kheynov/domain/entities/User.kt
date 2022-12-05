package ru.kheynov.domain.entities

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id") val userId: String,
    val username: String,
    val recipient: String? = null,
)

@Serializable
data class UserInfo(
    @SerialName("user_id") val userId: String,
    val username: String,
)

data class UserAuth(val userId: String = "", val displayName: String? = "") : Principal