package ru.kheynov.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import ru.kheynov.domain.entities.RoomDTO
import ru.kheynov.domain.entities.RoomDTO.*

interface RoomsRepository {

    data class RoomUpdate(
        val roomId: String,
        val update: Room,
    )

    val updates: Flow<RoomUpdate>

    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomById(id: String): Boolean
    suspend fun getRoomById(id: String): Room?
    suspend fun updateRoomById(id: String, newRoomData: RoomDTO.RoomUpdate): Boolean
    suspend fun getUserRooms(userId: String): List<RoomInfo>
}