package ru.kheynov.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kheynov.domain.entities.RoomDTO
import ru.kheynov.domain.entities.RoomDTO.Room
import ru.kheynov.domain.entities.RoomDTO.RoomInfo
import ru.kheynov.utils.UpdateModel

interface RoomsRepository {

    val updates: Flow<UpdateModel>

    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomById(id: String): Boolean
    suspend fun getRoomById(id: String): Room?
    suspend fun updateRoomById(id: String, newRoomData: RoomDTO.RoomUpdate): Boolean
    suspend fun getUserRooms(userId: String): List<RoomInfo>
}