package ru.kheynov.api.v1.requests

@kotlinx.serialization.Serializable
data class JoinRoomRequest(
    val name: String,
    val password: String,
)
