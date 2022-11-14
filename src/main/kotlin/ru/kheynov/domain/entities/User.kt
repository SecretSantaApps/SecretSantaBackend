package ru.kheynov.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val username: String,
)
