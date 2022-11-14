package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface UsersRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun deleteUserByID(userId: String): Boolean
    suspend fun getUserByID(userId: String): User?
    suspend fun updateUserByID(userId: String, name: String): Boolean
}