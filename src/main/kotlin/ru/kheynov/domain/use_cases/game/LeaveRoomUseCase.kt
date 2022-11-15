package ru.kheynov.domain.use_cases.game

import ru.kheynov.utils.GameRepositories

class LeaveRoomUseCase(
    gameRepositories: GameRepositories,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotInRoom : Result
        object RoomNotFound : Result
        object UserNotFound : Result
        object GameAlreadyStarted : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        if (roomsRepository.getRoomUsers(roomName).find { it.userId == userId } == null) return Result.UserNotInRoom
        if (room.gameStarted) return Result.GameAlreadyStarted
        return if (gameRepository.deleteFromRoom(
                roomName = roomName,
                userId = userId,
            )
        ) Result.Successful else Result.Failed
    }

}