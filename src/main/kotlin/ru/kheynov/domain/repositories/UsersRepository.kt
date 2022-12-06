package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.UserDTO

interface UsersRepository {
    suspend fun registerUser(user: UserDTO.User): Boolean
    suspend fun deleteUserByID(userId: String): Boolean
    suspend fun getUserByID(userId: String): UserDTO.User?
    suspend fun updateUserByID(userId: String, name: String): Boolean
}