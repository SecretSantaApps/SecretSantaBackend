package ru.kheynov.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val name: String,
    val password: String?,
)