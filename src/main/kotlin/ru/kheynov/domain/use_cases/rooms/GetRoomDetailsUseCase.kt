package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class GetRoomDetailsUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val room: Room) : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotExists
        if (room.ownerId != userId) return Result.Forbidden
        return Result.Successful(room)
    }
}