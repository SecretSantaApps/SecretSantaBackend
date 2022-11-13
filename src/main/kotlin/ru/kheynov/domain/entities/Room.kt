package ru.kheynov.domain.entities

import java.time.LocalDate

data class Room(
    val name: String,
    val password: String?,
    val date: LocalDate?,
    val ownerId: String,
    val maxPrice: Int? = null,
)
