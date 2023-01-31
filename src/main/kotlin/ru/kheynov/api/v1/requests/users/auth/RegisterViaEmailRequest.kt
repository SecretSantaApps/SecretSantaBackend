package ru.kheynov.api.v1.requests.users.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterViaEmailRequest(
    val username: String,
    val email: String,
    val password: String,
    val address: String?,
)