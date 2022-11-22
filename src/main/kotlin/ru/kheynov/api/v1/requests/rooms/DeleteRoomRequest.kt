package ru.kheynov.api.v1.requests.rooms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomRequest(
    @SerialName("room_name") val roomName: String,
)