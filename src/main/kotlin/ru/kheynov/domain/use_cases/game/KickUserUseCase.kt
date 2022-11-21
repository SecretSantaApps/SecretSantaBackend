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
        object GameAlreadyStarted : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        selfId: String, //ID of user that requested deletion
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        if (gameRepository.getUsersInRoom(roomName).find { it.userId == userId } == null) return Result.UserNotInRoom
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        if (room.ownerId != selfId) return Result.Forbidden
        if (room.gameStarted) return Result.GameAlreadyStarted
        return if (gameRepository.deleteFromRoom(
                roomName = roomName,
                userId = userId,
            )
        ) Result.Successful else Result.Failed
    }
}