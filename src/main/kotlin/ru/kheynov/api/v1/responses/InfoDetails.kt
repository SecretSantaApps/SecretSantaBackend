package ru.kheynov.api.v1.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kheynov.domain.entities.UserInfo

@Serializable
data class InfoDetails(
    @SerialName("room_name") val roomName: String,
    val users: List<UserInfo>,
    val recipient: String? = null,
)