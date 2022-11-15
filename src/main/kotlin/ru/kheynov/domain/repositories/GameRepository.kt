package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface GameRepository {
    suspend fun addToRoom(roomName: String, userId: String): Boolean
    suspend fun deleteFromRoom(roomName: String, userId: String): Boolean
    suspend fun addRecipient(roomName: String, userId: String, recipientId: String): Boolean
    suspend fun getUsersInRoom(roomName: String): List<User>
    suspend fun getUsersRecipient(roomName: String, userId: String): String?
}