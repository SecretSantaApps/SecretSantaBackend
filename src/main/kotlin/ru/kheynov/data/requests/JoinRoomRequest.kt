package ru.kheynov.data.requests

@kotlinx.serialization.Serializable
data class JoinRoomRequest(
    val name: String,
    val password: String,
)
