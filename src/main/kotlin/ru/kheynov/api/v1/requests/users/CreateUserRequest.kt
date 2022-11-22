package ru.kheynov.api.v1.requests.users

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(val username: String?)