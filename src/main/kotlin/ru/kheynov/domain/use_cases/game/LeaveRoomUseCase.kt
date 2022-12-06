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
        roomId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (gameRepository.getUsersInRoom(roomId).find { it.userId == userId } == null) return Result.UserNotInRoom
        if (room.gameStarted) return Result.GameAlreadyStarted

        var res = gameRepository.deleteFromRoom(
            roomId = roomId,
            userId = userId,
        )

        if (room.ownerId == userId) {
            res = res && roomsRepository.deleteRoomById(roomId)
        }

        return if (res) Result.Successful else Result.Failed
    }
}