package ru.kheynov.domain.repositories

import ru.kheynov.data.entities.RefreshToken
import ru.kheynov.domain.entities.UserDTO

interface UsersRepository {
    suspend fun registerUser(user: UserDTO.User): Boolean
    suspend fun deleteUserByID(userId: String): Boolean
    suspend fun getUserByID(userId: String): UserDTO.User?
    suspend fun updateUserByID(userId: String, name: String): Boolean
    suspend fun getUserByEmail(email: String): UserDTO.User?
    suspend fun updateUserRefreshToken(
        oldRefreshToken: String,
        newRefreshToken: String,
        refreshTokenExpiration: Long,
    ): Boolean

    suspend fun getRefreshTokenByUserId(userId: String): ru.kheynov.security.jwt.token.RefreshToken?

    suspend fun createRefreshToken(
        userId: String,
        refreshToken: ru.kheynov.security.jwt.token.RefreshToken,
    ): Boolean
}