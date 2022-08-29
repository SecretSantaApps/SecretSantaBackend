package ru.kheynov.api.v1.responses

@kotlinx.serialization.Serializable
data class RoomInfoResponse(
    val name: String,
    val password: String?,
    val creatorId: String,
    val userIds: List<String>,
    val giftPrice: Int?,
)