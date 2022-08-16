package ru.kheynov.api.v1.requests

@kotlinx.serialization.Serializable
data class LeaveRoomRequest(
    val name: String,
)