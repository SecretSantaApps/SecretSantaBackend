package ru.kheynov.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val username: String,
    val recipient: String? = null,
)

@Serializable
data class UserInfo(
    val userId: String,
    val username: String,
)