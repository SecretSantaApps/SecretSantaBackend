package ru.kheynov.domain.use_cases.game

import ru.kheynov.utils.GameRepositories

class StopGameUseCase(
    gameRepositories: GameRepositories,
) {
    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object RoomNotFound : Result
        object Forbidden : Result
        object UserNotFound : Result
        object GameAlreadyStopped : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (room.ownerId != userId) return Result.Forbidden
        if (!room.gameStarted) return Result.GameAlreadyStopped
        val res = gameRepository.setGameState(roomId, false) && gameRepository.deleteRecipients(roomId)
        return if (res) Result.Successful else Result.Failed
    }
}