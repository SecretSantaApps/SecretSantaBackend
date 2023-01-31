package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.security.jwt.token.RefreshToken

interface UsersRepository {
    suspend fun registerUser(user: UserDTO.User): Boolean
    suspend fun deleteUserByID(userId: String): Boolean
    suspend fun getUserByID(userId: String): UserDTO.UserInfo?
    suspend fun updateUserByID(userId: String, name: String?, address: String?): Boolean
    suspend fun getUserByEmail(email: String): UserDTO.User?

    suspend fun updateUserRefreshToken(
        userId: String,
        clientId: String,
        newRefreshToken: String,
        refreshTokenExpiration: Long,
    ): Boolean

    suspend fun getRefreshToken(oldRefreshToken: String): UserDTO.RefreshTokenInfo?

    suspend fun getRefreshTokenByUserId(userId: String): UserDTO.RefreshTokenInfo?

    suspend fun createRefreshToken(
        userId: String,
        clientId: String,
        refreshToken: RefreshToken,
    ): Boolean
}