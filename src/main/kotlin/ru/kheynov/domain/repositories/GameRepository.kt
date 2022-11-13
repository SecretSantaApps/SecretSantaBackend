package ru.kheynov.domain.repositories

interface GameRepository {
    fun addRecipient(roomId: Int, userId: String): Boolean
}