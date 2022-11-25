package ru.kheynov.data.repositories.rooms

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import ru.kheynov.data.entities.RoomMembers
import ru.kheynov.data.entities.Rooms
import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.entities.RoomInfo
import ru.kheynov.domain.entities.RoomUpdate
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.utils.mapToRoom

class PostgresRoomsRepository(
    private val database: Database,
) : RoomsRepository {

    override suspend fun createRoom(room: Room): Boolean {
        val newRoom = ru.kheynov.data.entities.Room {
            name = room.name
            password = room.password
            date = room.date
            maxPrice = room.maxPrice
            ownerId = room.ownerId
            gameStarted = false
        }
        val affectedRows = database.sequenceOf(Rooms).add(newRoom)
        return affectedRows == 1
    }

    override suspend fun deleteRoomByName(name: String): Boolean {
        val affectedRows = database.sequenceOf(Rooms).find { it.name eq name }?.delete()
        return affectedRows == 1
    }

    override suspend fun getRoomByName(name: String): Room? =
        database.sequenceOf(Rooms).find { it.name eq name }?.mapToRoom()

    override suspend fun updateRoomByName(name: String, newRoomData: RoomUpdate): Boolean {
        val room = database.sequenceOf(Rooms).find { it.name eq name } ?: return false
        room.password = newRoomData.password ?: room.password
        room.date = newRoomData.date ?: room.date
        room.maxPrice = newRoomData.maxPrice ?: room.maxPrice
        val affectedRows = room.flushChanges()
        return affectedRows == 1
    }

    override suspend fun getUserRooms(userId: String): List<RoomInfo> =
        database.from(Rooms).innerJoin(RoomMembers, on = Rooms.name eq RoomMembers.roomName).select(
            Rooms.name,
            Rooms.date,
            Rooms.ownerId,
            Rooms.ownerId,
            Rooms.gameStarted,
        ).where {
            RoomMembers.userId eq userId
        }.map { room ->
            val membersCount =
                database.sequenceOf(RoomMembers)
                    .filter { it.roomName eq (room[Rooms.name] ?: "") }
                    .aggregateColumns { count(it.userId) }
            RoomInfo(
                room[Rooms.name] ?: "",
                room[Rooms.date],
                room[Rooms.ownerId] ?: "",
                room[Rooms.maxPrice],
                room[Rooms.gameStarted] ?: false,
                membersCount ?: 0,
            )
        }
}