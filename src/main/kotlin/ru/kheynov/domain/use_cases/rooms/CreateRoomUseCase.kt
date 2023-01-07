package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.RoomDTO
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.utils.getRandomPassword
import ru.kheynov.utils.getRandomRoomID
import java.time.LocalDate

class CreateRoomUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val room: RoomDTO.Room) : Result
        object UserNotExists : Result
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
        val room = RoomDTO.Room(
            name = roomName,
            password = if (password.isNullOrBlank()) getRandomPassword() else password,
            id = getRandomRoomID(),
            date = date,
            ownerId = userId,
            maxPrice = maxPrice,
            gameStarted = false,
            membersCount = 1
        )
        return if (roomsRepository.createRoom(room)) Result.Successful(room) else Result.Failed
    }
}