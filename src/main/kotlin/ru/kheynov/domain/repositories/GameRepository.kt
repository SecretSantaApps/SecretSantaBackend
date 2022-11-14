package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface GameRepository {
    fun joinRoom(roomId: Int, userId: String): Boolean
    fun addRecipient(roomId: Int, userId: String, recipientId: String): Boolean
    fun getUsersInRoom(roomId: Int): List<User>
    fun getUsersRecipient(roomId: Int, userId: String): String?
}