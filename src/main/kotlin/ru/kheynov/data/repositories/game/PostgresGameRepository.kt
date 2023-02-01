package ru.kheynov.data.repositories.game

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.*
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.repositories.GameRepository

class PostgresGameRepository(
    private val database: Database,
) : GameRepository {
    override suspend fun addToRoom(roomId: String, userId: String, wishlist: String?): Boolean {
        val newMember = RoomMember {
            this.roomId = database.sequenceOf(Rooms).find { it.id eq roomId } ?: return false
            this.userId = database.sequenceOf(Users).find { it.userId eq userId } ?: return false
            this.wishlist = wishlist
        }
        val affectedRows = database.sequenceOf(RoomMembers).add(newMember)
        return affectedRows == 1
    }

    override suspend fun deleteFromRoom(roomId: String, userId: String): Boolean {
        val affectedRows = database.delete(RoomMembers) { (it.userId eq userId) and (it.roomId eq roomId) }
        return affectedRows == 1
    }

    override suspend fun setRecipient(roomId: String, userId: String, recipientId: String): Boolean {
        val affectedRows = database.update(RoomMembers) {
            set(it.recipient, recipientId)
            where {
                (it.userId eq userId) and (it.roomId eq roomId)
            }
        }
        return affectedRows == 1
    }

    override suspend fun deleteRecipients(roomId: String): Boolean {
        val affectedRows = database.update(RoomMembers) {
            set(it.recipient, null)
            where { it.roomId eq roomId }
        }
        return affectedRows > 0
    }

    override suspend fun getUsersInRoom(roomId: String): List<UserDTO.UserRoomInfo> {
        return database.from(RoomMembers)
            .innerJoin(Users, on = RoomMembers.userId eq Users.userId)
            .innerJoin(Avatars, on = Users.avatar eq Avatars.id)
            .select(Users.userId, Users.name).where {
                RoomMembers.roomId eq roomId
            }.map { row ->
                UserDTO.UserRoomInfo(
                    userId = row[Users.userId]!!,
                    username = row[Users.name]!!,
                    address = row[Users.address]!!,
                    wishlist = row[RoomMembers.wishlist]!!,
                    avatar = row[Avatars.image]!!
                )
            }
    }

    override suspend fun getUsersRecipient(roomId: String, userId: String): String? {
        return database.sequenceOf(RoomMembers)
            .find { (it.userId eq userId) and (it.roomId eq roomId) }?.recipient?.userId
    }

    override suspend fun setGameState(roomId: String, state: Boolean): Boolean {
        val affectedRows = database.update(Rooms) {
            set(it.gameStarted, state)
            where {
                it.id eq roomId
            }
        }
        return affectedRows == 1
    }

    override suspend fun checkUserInRoom(roomId: String, userId: String): Boolean {
        return database.sequenceOf(RoomMembers).find { (it.roomId eq roomId) and (it.userId eq userId) } != null
    }
}