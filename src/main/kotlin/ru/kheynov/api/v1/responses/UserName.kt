package ru.kheynov.api.v1.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserName(
    val username: String,
)
