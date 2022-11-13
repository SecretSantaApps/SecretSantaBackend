package ru.kheynov.data.repositories.users

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.Users
import ru.kheynov.data.entities.mapToUser
import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UsersRepository

class PostgresUsersRepository(
    private val database: Database,
) : UsersRepository {
    override suspend fun registerUser(user: User): Boolean {
        val newUser = ru.kheynov.data.entities.User {
            userId = user.userId
            name = user.username
        }

        val affectedRows = database.sequenceOf(Users).add(newUser)
        return affectedRows == 1
    }

    override suspend fun getUserByID(userId: String): User? =
        database.sequenceOf(Users).find { user -> user.userId eq userId }?.mapToUser()

    override suspend fun deleteUserByID(userId: String): Boolean {
        val affectedRows = database.sequenceOf(Users).find { user -> user.userId eq userId }?.delete()
        return affectedRows == 1
    }

    override suspend fun updateUserByID(userId: String, user: User): Boolean {
        val foundUser = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
        foundUser.name = user.username
        foundUser.userId = user.userId
        val affectedRows = foundUser.flushChanges()
        return affectedRows == 1
    }
}