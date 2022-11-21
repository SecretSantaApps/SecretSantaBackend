package ru.kheynov.api.v1.requests.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetGameInfoDetailsRequest(
    @SerialName("room_name") val roomName: String,
)