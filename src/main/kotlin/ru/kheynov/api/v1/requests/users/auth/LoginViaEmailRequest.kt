package ru.kheynov.api.v1.requests.users.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginViaEmailRequest(
    val email: String,
    val password: String,
)