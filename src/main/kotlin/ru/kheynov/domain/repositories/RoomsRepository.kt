package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.RoomDTO.*

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomById(id: String): Boolean
    suspend fun getRoomById(id: String): Room?
    suspend fun updateRoomById(id: String, newRoomData: RoomUpdate): Boolean
    suspend fun getUserRooms(userId: String): List<RoomInfo>
}