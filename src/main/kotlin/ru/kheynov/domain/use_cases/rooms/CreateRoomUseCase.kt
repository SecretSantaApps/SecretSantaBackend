package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.getRandomPassword
import java.time.LocalDate

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
            password = if (password.isNullOrBlank()) getRandomPassword() else password,
            name = roomName,
            date = date,
            ownerId = userId,
            maxPrice = maxPrice,
            gameStarted = false
        )
        return if (roomsRepository.createRoom(room)) Result.Successful(room) else Result.Failed
    }
}