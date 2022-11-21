package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface GameRepository {
    suspend fun addToRoom(roomName: String, userId: String): Boolean
    suspend fun deleteFromRoom(roomName: String, userId: String): Boolean
    suspend fun setRecipient(roomName: String, userId: String, recipientId: String?): Boolean
    suspend fun deleteRecipients(roomName: String): Boolean
    suspend fun getUsersInRoom(roomName: String): List<User>
    suspend fun getUsersRecipient(roomName: String, userId: String): String?
    suspend fun setGameState(roomName: String, state: Boolean): Boolean
    suspend fun checkUserInRoom(roomName: String, userId: String): Boolean
}