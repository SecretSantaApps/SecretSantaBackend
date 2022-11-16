package ru.kheynov.api.v1.requests.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KickUserRequest(
    @SerialName("user_id") val userId: String,
    @SerialName("room_name") val roomName: String
)