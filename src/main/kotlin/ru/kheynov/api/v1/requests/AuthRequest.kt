package ru.kheynov.api.v1.requests

@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val password: String,
)