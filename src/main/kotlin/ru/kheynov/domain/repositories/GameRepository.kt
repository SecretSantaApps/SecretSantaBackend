package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.UserDTO

interface GameRepository {
    suspend fun addToRoom(roomId: String, userId: String, wishlist: String?): Boolean
    suspend fun deleteFromRoom(roomId: String, userId: String): Boolean
    suspend fun setRecipient(roomId: String, userId: String, recipientId: String): Boolean
    suspend fun deleteRecipients(roomId: String): Boolean
    suspend fun getUsersInRoom(roomId: String): List<UserDTO.UserInfo>
    suspend fun getUsersRecipient(roomId: String, userId: String): String?
    suspend fun setGameState(roomId: String, state: Boolean): Boolean
    suspend fun checkUserInRoom(roomId: String, userId: String): Boolean
}