package ru.kheynov.data.mappers

import ru.kheynov.data.entities.Room
import ru.kheynov.domain.entities.RoomDTO

fun Room.mapToRoom(): RoomDTO.Room = RoomDTO.Room(
    name = this.name,
    id = this.id,
    date = this.date,
    ownerId = this.ownerId,
    playableOwner = this.playableOwner,
    maxPrice = this.maxPrice,
    gameState = this.gameState,
    membersCount = 1
)
