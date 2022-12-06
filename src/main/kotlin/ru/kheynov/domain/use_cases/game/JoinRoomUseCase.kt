package ru.kheynov.domain.use_cases.game

import ru.kheynov.utils.GameRepositories

class JoinRoomUseCase(
    gameRepositories: GameRepositories,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object Forbidden : Result
        object RoomNotFound : Result
        object UserNotFound : Result
        object GameAlreadyStarted : Result
        object UserAlreadyInRoom : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        userId: String,
        roomId: String,
        password: String?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (gameRepository.checkUserInRoom(roomId, userId)) return Result.UserAlreadyInRoom
        if (room.gameStarted) return Result.GameAlreadyStarted
        return if (room.password == password) {
            if (gameRepository.addToRoom(room.id, userId)) Result.Successful
            else Result.Failed
        } else Result.Forbidden
    }
}