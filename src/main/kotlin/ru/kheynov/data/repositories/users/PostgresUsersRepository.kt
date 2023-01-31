package ru.kheynov.data.repositories.users

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.RefreshTokens
import ru.kheynov.data.entities.Users
import ru.kheynov.data.mappers.*
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

    override suspend fun getUserByID(userId: String): UserDTO.UserInfo? =
        database.sequenceOf(Users).find { user -> user.userId eq userId }?.mapToUserInfo()

    override suspend fun deleteUserByID(userId: String): Boolean {
        val affectedRows = database.sequenceOf(Users).find { user -> user.userId eq userId }?.delete()
        return affectedRows == 1
    }

    override suspend fun updateUserByID(userId: String, name: String?, address: String?): Boolean {
        val foundUser = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
        if (name != null) foundUser.name = name
        foundUser.address = address
        val affectedRows = foundUser.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getUserByEmail(email: String): UserDTO.User? {
        val foundUser = database.sequenceOf(Users).find { it.email eq email } ?: return null
        return foundUser.mapToUser()
    }

    override suspend fun updateUserRefreshToken(
        userId: String,
        clientId: String,
        newRefreshToken: String,
        refreshTokenExpiration: Long,
    ): Boolean {
        val foundUser = database.sequenceOf(RefreshTokens).find { (it.userId eq userId) and (it.clientId eq clientId) }
            ?: return false
        foundUser.refreshToken = newRefreshToken
        foundUser.expiresAt = refreshTokenExpiration
        val affectedRows = foundUser.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getRefreshToken(oldRefreshToken: String): UserDTO.RefreshTokenInfo? {
        return database.sequenceOf(RefreshTokens).find { oldRefreshToken eq it.refreshToken }
            ?.toRefreshTokenInfo()
    }

    override suspend fun getRefreshTokenByUserId(userId: String): UserDTO.RefreshTokenInfo? {
        return database.sequenceOf(RefreshTokens).find { userId eq it.userId }
            ?.toRefreshTokenInfo()
    }

    override suspend fun createRefreshToken(userId: String, clientId: String, refreshToken: RefreshToken): Boolean {
        val affectedRows = database.sequenceOf(RefreshTokens).add(refreshToken.toDataRefreshToken(userId, clientId))
        return affectedRows == 1
    }
}
