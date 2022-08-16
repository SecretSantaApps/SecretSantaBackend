package ru.kheynov.api.v1.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String,
)
