package ru.kheynov.utils

import ru.kheynov.data.entities.Room
import ru.kheynov.data.entities.User
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

typealias GameRepositories = Triple<UsersRepository, RoomsRepository, GameRepository>

fun Room.mapToRoom(): ru.kheynov.domain.entities.Room {
    return ru.kheynov.domain.entities.Room(
        this.name, this.password, this.date, this.ownerId, this.maxPrice, this.gameStarted, membersCount = 1
    )
}

fun User.mapToUser(): ru.kheynov.domain.entities.User {
    return ru.kheynov.domain.entities.User(this.userId, this.name, null)
}