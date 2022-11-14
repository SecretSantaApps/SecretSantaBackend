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
    override fun joinRoom(roomId: Int, userId: String): Boolean {
        val newMember = RoomMember {
            this.roomId = database.sequenceOf(Rooms).find { it.id eq roomId } ?: return false
            this.userId = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
        }
        val affectedRows = database.sequenceOf(RoomMembers).add(newMember)
        return affectedRows == 1
    }

    override fun addRecipient(roomId: Int, userId: String, recipientId: String): Boolean {
        val affectedRows = database.update(RoomMembers) {
            set(it.recipient, recipientId)
            where {
                (it.userId eq userId) and (it.roomId eq roomId)
            }
        }
        return affectedRows == 1
    }

    override fun getUsersInRoom(roomId: Int): List<User> {
        return database.from(RoomMembers)
            .innerJoin(Users, on = RoomMembers.userId eq Users.userId)
            .innerJoin(Rooms, on = RoomMembers.roomId eq Rooms.id)
            .select(Users.userId, Users.name).map { row ->
                User(
                    row[Users.userId] ?: "",
                    row[Users.name] ?: "",
                )
            }
    }

    override fun getUsersRecipient(roomId: Int, userId: String): String? {
        return database.sequenceOf(RoomMembers)
            .find { (it.userId eq userId) and (it.roomId eq roomId) }?.recipient?.userId
    }
}