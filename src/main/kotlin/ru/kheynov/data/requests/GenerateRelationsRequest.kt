package ru.kheynov.data.requests

@kotlinx.serialization.Serializable
data class GenerateRelationsRequest(
    val name: String,
)