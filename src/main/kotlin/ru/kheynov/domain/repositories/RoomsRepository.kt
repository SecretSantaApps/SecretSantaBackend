package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.Room

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean

    suspend fun deleteRoomByName(name: String): Boolean

    suspend fun addUserToRoom(vararg userIds: String, roomName: String): Boolean

    suspend fun deleteUserFromRoom(userId: String, roomName: String): Boolean

    suspend fun getRoomByName(name: String): Room?
}