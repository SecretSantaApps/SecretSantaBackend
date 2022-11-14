package ru.kheynov.domain.use_cases.game

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class JoinRoomUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
    private val gameRepository: GameRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object Forbidden : Result
        object RoomNotFound : Result
        object UserNotFound : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
        password: String?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        return if (room.ownerId == userId || room.password == password) {
            if (gameRepository.joinRoom(room.id ?: return Result.Failed, userId))
                Result.Successful
            else
                Result.Failed
        } else
            Result.Forbidden
    }
}