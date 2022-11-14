package ru.kheynov.api.v1.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val name: String,
    val password: String?,
    @SerialName("max_price") val maxPrice: Int?,
)