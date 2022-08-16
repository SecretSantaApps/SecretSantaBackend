package ru.kheynov.api.v1.responses

@kotlinx.serialization.Serializable
data class UserInfoResponse(
    val username: String,
    val id: String,
)