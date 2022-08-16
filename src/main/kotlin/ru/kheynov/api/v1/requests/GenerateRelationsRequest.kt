package ru.kheynov.api.v1.requests

@kotlinx.serialization.Serializable
data class GenerateRelationsRequest(
    val name: String,
)