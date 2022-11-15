package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.RoomUpdate
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class UpdateRoomUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Forbidden : Result
        object Failed : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
        roomUpdate: RoomUpdate,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotExists
        if (room.ownerId != userId) return Result.Forbidden
        return if (roomsRepository.updateRoomByName(
                roomName,
                roomUpdate
            )
        ) Result.Successful else Result.Failed
    }
}