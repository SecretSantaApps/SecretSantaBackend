package ru.kheynov.data.repositories.rooms

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import ru.kheynov.data.entities.RoomMembers
import ru.kheynov.data.entities.Rooms
import ru.kheynov.data.mappers.mapToRoom
import ru.kheynov.domain.entities.RoomDTO.*
import ru.kheynov.domain.repositories.RoomsRepository

class PostgresRoomsRepository(
    private val database: Database,
) : RoomsRepository {

    override suspend fun createRoom(room: Room): Boolean {
        var newRoom = ru.kheynov.data.entities.Room {
            id = room.id
            name = room.name
            password = room.password
            date = room.date
            maxPrice = room.maxPrice
            ownerId = room.ownerId
            gameStarted = false
        }
        var affectedRows = database.sequenceOf(Rooms).add(newRoom)
        if (affectedRows != 1) { // if failed, try to change UUID
            newRoom = newRoom.copy()
            newRoom.id = room.id + 1
            affectedRows = database.sequenceOf(Rooms).add(newRoom)
        }
        return affectedRows == 1
    }

    override suspend fun deleteRoomById(id: String): Boolean {
        val affectedRows = database.sequenceOf(Rooms).find { it.id eq id }?.delete()
        return affectedRows == 1
    }

    override suspend fun getRoomById(id: String): Room? {
        val membersCount = database.sequenceOf(RoomMembers).filter { it.roomId eq id }
            .aggregateColumns { count(it.userId) }
        return database.sequenceOf(Rooms).find { it.id eq id }?.mapToRoom()?.copy(membersCount = membersCount ?: 1)
    }

    override suspend fun updateRoomById(id: String, newRoomData: RoomUpdate): Boolean {
        val room = database.sequenceOf(Rooms).find { it.id eq id } ?: return false
        room.password = newRoomData.password ?: room.password
        room.date = newRoomData.date ?: room.date
        room.maxPrice = newRoomData.maxPrice ?: room.maxPrice
        val affectedRows = room.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getUserRooms(userId: String): List<RoomInfo> =
        database.from(Rooms)
            .innerJoin(RoomMembers, on = Rooms.id eq RoomMembers.roomId).select(
                Rooms.name,
                Rooms.id,
                Rooms.date,
                Rooms.ownerId,
                Rooms.ownerId,
                Rooms.gameStarted,
            ).where {
                RoomMembers.userId eq userId
            }.map { room ->
                val membersCount = database.sequenceOf(RoomMembers).filter { it.roomId eq (room[Rooms.id] ?: "") }
                    .aggregateColumns { count(it.userId) }
                RoomInfo(
                    name = room[Rooms.name] ?: "",
                    id = room[Rooms.id] ?: "",
                    date = room[Rooms.date],
                    ownerId = room[Rooms.ownerId] ?: "",
                    maxPrice = room[Rooms.maxPrice],
                    gameStarted = room[Rooms.gameStarted] ?: false,
                    membersCount = membersCount ?: 0,
                )
            }
}
