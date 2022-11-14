package ru.kheynov.api.v1.requests.rooms

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomDetailsRequest(
    val name: String,
)