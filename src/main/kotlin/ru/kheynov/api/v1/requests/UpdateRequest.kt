package ru.kheynov.api.v1.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest(
    val username: String? = null,
    val password: String? = null,
)
