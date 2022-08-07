package ru.kheynov.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest(
    val username: String? = null,
    val password: String? = null,
)
