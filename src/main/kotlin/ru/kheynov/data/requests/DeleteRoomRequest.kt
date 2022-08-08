package ru.kheynov.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomRequest(
    val name: String,
)