package ru.kheynov.data.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String,
)
