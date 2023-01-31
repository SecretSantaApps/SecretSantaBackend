package ru.kheynov.data.mappers

import ru.kheynov.data.entities.Room
import ru.kheynov.domain.entities.RoomDTO

fun Room.mapToRoom(): RoomDTO.Room = RoomDTO.Room(
    name = this.name,
    id = this.id,
    password = this.password,
    date = this.date,
    ownerId = this.ownerId,
    playableOwner = this.playableOwner,
    maxPrice = this.maxPrice,
    gameStarted = this.gameStarted,
    membersCount = 1
)
