package ru.kheynov.domain.repositories

import ru.kheynov.domain.entities.Room

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun deleteRoomById(id: Int): Boolean
    suspend fun getRoomById(id: Int): Room?
}