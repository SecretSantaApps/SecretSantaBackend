package ru.kheynov.domain.use_cases.game

import ru.kheynov.utils.GameRepositories

class KickUserUseCase(
    gameRepositories: GameRepositories,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object Forbidden : Result
        object UserNotInRoom : Result
        object RoomNotFound : Result
        object UserNotFound : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        ownerId: String,
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        if (roomsRepository.getRoomUsers(roomName).find { it.userId == userId } == null) return Result.UserNotInRoom
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        if (room.ownerId != ownerId) return Result.Forbidden
        return if (gameRepository.deleteFromRoom(
                roomName = roomName,
                userId = userId,
            )
        ) Result.Successful else Result.Failed
    }
}