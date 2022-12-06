package ru.kheynov.domain.entities

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface UserDTO {
    @Serializable
    data class User(
        @SerialName("user_id") val userId: String,
        val username: String,
    ) : UserDTO
}

data class UserAuth(val userId: String = "", val displayName: String? = "") : Principal
