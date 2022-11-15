package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.entities.RoomUpdate
import ru.kheynov.domain.entities.User

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomByName(name: String): Boolean
    suspend fun getRoomByName(name: String): Room?
    suspend fun getRoomUsers(name: String): List<User>
    suspend fun updateRoomByName(name: String, newRoomData: RoomUpdate): Boolean
}