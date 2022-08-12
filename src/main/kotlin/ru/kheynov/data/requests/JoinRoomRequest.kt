package ru.kheynov.data.requests

@kotlinx.serialization.Serializable
data class JoinRoomRequest(
    val roomName: String,
    val password: String,
)
