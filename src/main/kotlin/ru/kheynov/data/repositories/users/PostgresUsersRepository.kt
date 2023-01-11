package ru.kheynov.data.repositories.users

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.RefreshTokens
import ru.kheynov.data.entities.Users
import ru.kheynov.data.mappers.mapToUser
import ru.kheynov.data.mappers.toDataRefreshToken
import ru.kheynov.data.mappers.toDataUser
import ru.kheynov.data.mappers.toRefreshToken
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.security.jwt.token.RefreshToken

class PostgresUsersRepository(
    private val database: Database,
) : UsersRepository {
    override suspend fun registerUser(user: UserDTO.User): Boolean {
        val affectedRows = database.sequenceOf(Users).add(toDataUser(user))
        return affectedRows == 1
    }

    override suspend fun getUserByID(userId: String): UserDTO.User? =
        database.sequenceOf(Users).find { user -> user.userId eq userId }?.mapToUser()

    override suspend fun deleteUserByID(userId: String): Boolean {
        val affectedRows = database.sequenceOf(Users).find { user -> user.userId eq userId }?.delete()
        return affectedRows == 1
    }

    override suspend fun updateUserByID(userId: String, name: String): Boolean {
        val foundUser = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
        foundUser.name = name
        val affectedRows = foundUser.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getUserByEmail(email: String): UserDTO.User? {
        val foundUser = database.sequenceOf(Users).find { it.email eq email } ?: return null
        return foundUser.mapToUser()
    }

    override suspend fun updateUserRefreshToken(
        oldRefreshToken: String,
        newRefreshToken: String,
        refreshTokenExpiration: Long,
    ): Boolean {
        val foundUser = database.sequenceOf(RefreshTokens).find { it.refreshToken eq oldRefreshToken } ?: return false
        foundUser.refreshToken = newRefreshToken
        foundUser.expiresAt = refreshTokenExpiration
        val affectedRows = foundUser.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getRefreshTokenByUserId(userId: String): RefreshToken? {
        return database.sequenceOf(RefreshTokens).find { it.userId eq userId }?.toRefreshToken()
    }

    override suspend fun createRefreshToken(userId: String, refreshToken: RefreshToken): Boolean {
        val affectedRows = database.sequenceOf(RefreshTokens).add(refreshToken.toDataRefreshToken(userId))
        return affectedRows == 1
    }
}
