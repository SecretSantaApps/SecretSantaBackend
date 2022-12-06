package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.RoomDTO
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
        roomId: String,
        roomUpdate: RoomDTO.RoomUpdate,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (room.ownerId != userId) return Result.Forbidden
        return if (roomsRepository.updateRoomById(
                roomId,
                roomUpdate
            )
        ) Result.Successful else Result.Failed
    }
}