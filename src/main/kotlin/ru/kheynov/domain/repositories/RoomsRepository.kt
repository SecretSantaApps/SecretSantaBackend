package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.entities.RoomUpdate

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomByName(name: String): Boolean
    suspend fun getRoomByName(name: String): Room?
    suspend fun updateRoomByName(name: String, newRoomData: RoomUpdate): Boolean
}