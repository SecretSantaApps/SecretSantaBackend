package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import java.time.LocalDate
import java.util.*

class CreateRoomUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val room: Room) : Result
        object UserNotExists : Result
        object RoomAlreadyExists : Result
        object Failed : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
        password: String?,
        date: LocalDate?,
        maxPrice: Int?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        if (roomsRepository.getRoomByName(roomName) != null) return Result.RoomAlreadyExists
        val room = Room(
            password = password ?: getRandomPassword(),
            name = roomName,
            date = date,
            ownerId = userId,
            maxPrice = maxPrice,
            gameStarted = false
        )
        return if (roomsRepository.createRoom(room)) Result.Successful(room) else Result.Failed
    }
}

private fun getRandomPassword(): String = UUID.randomUUID().toString().subSequence(0..6).toString()
