package ru.kheynov.utils

import ru.kheynov.data.entities.Room
import ru.kheynov.data.entities.User
import ru.kheynov.domain.entities.RoomDTO
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

typealias GameRepositories = Triple<UsersRepository, RoomsRepository, GameRepository>

fun Room.mapToRoom(): RoomDTO.Room = RoomDTO.Room(
    name = this.name,
    id = this.id,
    password = this.password,
    date = this.date,
    ownerId = this.ownerId,
    maxPrice = this.maxPrice,
    gameStarted = this.gameStarted,
    membersCount = 1
)


fun User.mapToUser(): UserDTO.User = UserDTO.User(userId = this.userId, username = this.name)
