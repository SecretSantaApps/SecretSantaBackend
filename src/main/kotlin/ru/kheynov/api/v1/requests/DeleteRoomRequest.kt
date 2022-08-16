package ru.kheynov.api.v1.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomRequest(
    val name: String,
)