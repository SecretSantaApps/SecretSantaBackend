package ru.kheynov.data.repositories.game

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.RoomMember
import ru.kheynov.data.entities.RoomMembers
import ru.kheynov.data.entities.Rooms
import ru.kheynov.data.entities.Users
import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.GameRepository

class PostgresGameRepository(
    private val database: Database,
) : GameRepository {
    override suspend fun addToRoom(roomName: String, userId: String): Boolean {
        val newMember = RoomMember {
            this.roomName = database.sequenceOf(Rooms).find { it.name eq roomName } ?: return false
            this.userId = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
        }
        val affectedRows = database.sequenceOf(RoomMembers).add(newMember)
        return affectedRows == 1
    }

    override suspend fun deleteFromRoom(roomName: String, userId: String): Boolean {
        val affectedRows = database.delete(RoomMembers) { (it.userId eq userId) and (it.roomName eq roomName) }
        return affectedRows == 1
    }

    override suspend fun addRecipient(roomName: String, userId: String, recipientId: String): Boolean {
        val affectedRows = database.update(RoomMembers) {
            set(it.recipient, recipientId)
            where {
                (it.userId eq userId) and (it.roomName eq roomName)
            }
        }
        return affectedRows == 1
    }

    override suspend fun getUsersInRoom(roomName: String): List<User> {
        return database.from(RoomMembers).innerJoin(Users, on = RoomMembers.userId eq Users.userId)
            .innerJoin(Rooms, on = RoomMembers.roomName eq Rooms.name).select(Users.userId, Users.name).map { row ->
                User(
                    row[Users.userId] ?: "",
                    row[Users.name] ?: "",
                )
            }
    }

    override suspend fun getUsersRecipient(roomName: String, userId: String): String? {
        return database.sequenceOf(RoomMembers)
            .find { (it.userId eq userId) and (it.roomName eq roomName) }?.recipient?.userId
    }

    override suspend fun setGameState(roomName: String, state: Boolean): Boolean {
        val affectedRows = database.update(Rooms) {
            set(it.gameStarted, state)
            where {
                it.name eq roomName
            }
        }
        return affectedRows == 1
    }
}