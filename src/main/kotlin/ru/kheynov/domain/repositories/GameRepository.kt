package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface GameRepository {
    fun joinRoom(roomName: String, userId: String): Boolean
    fun addRecipient(roomName: String, userId: String, recipientId: String): Boolean
    fun getUsersInRoom(roomName: String): List<User>
    fun getUsersRecipient(roomName: String, userId: String): String?
}