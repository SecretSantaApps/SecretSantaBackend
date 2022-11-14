package ru.kheynov.data.repositories.rooms

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.kheynov.data.entities.Rooms
import ru.kheynov.data.entities.mapToRoom
import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository

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
}