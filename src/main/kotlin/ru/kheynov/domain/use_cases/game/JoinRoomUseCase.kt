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
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        userId: String,
        roomName: String,
        password: String?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        return if (room.password == password) {
            if (gameRepository.addToRoom(room.name, userId)) Result.Successful
            else Result.Failed
        } else Result.Forbidden
    }
}