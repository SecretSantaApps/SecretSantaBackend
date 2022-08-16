package ru.kheynov.api.v1.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val name: String,
    val password: String?,
)