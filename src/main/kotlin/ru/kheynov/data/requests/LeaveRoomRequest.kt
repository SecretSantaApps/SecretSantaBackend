package ru.kheynov.data.requests

@kotlinx.serialization.Serializable
data class LeaveRoomRequest(
    val name: String,
)