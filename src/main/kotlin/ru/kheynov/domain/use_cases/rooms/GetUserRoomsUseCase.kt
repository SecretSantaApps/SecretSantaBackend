package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.RoomInfo
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class GetUserRoomsUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val rooms: List<RoomInfo>) : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(
        userId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val rooms = roomsRepository.getUserRooms(userId)
        return Result.Successful(rooms)
    }
}