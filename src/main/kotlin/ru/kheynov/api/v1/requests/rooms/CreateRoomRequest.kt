package ru.kheynov.api.v1.requests.rooms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("room_name") val name: String,
    val password: String?,
    @SerialName("max_price") val maxPrice: Int?,
)