package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.User

interface UserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun deleteUserByID(id: String): Boolean
    suspend fun getUserByID(id: String): User?
    suspend fun updateUsernameByID(id: String, username: String): Boolean
    suspend fun updatePasswordByID(id: String, password: String, salt: String): Boolean
    suspend fun updateUserByID(id: String, user: User): Boolean
}