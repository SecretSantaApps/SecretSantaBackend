package ru.kheynov.api.v1.requests.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserDetailsRequest(
    @SerialName("room_name") val roomName: String,
    @SerialName("user_id")val userId: String?,
)