package ru.kheynov.api.v1.requests.rooms

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomRequest(
    val name: String,
)