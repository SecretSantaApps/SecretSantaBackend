package ru.kheynov.domain.entities

@kotlinx.serialization.Serializable
data class Room(
    val name: String,
    val password: String?,
    val creatorId: String,
    val userIds: List<String>,
    val giftPrice: Int? = null,
    val relations: Map<String, String>? = null,
)
