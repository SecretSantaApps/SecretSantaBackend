package ru.kheynov.data.responses

@kotlinx.serialization.Serializable
data class RoomInfoResponse(
    val name: String,
    val password: String?,
    val creatorId: String,
    val userIds: List<String>,
)