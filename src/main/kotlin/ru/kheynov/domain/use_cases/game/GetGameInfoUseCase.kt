package ru.kheynov.domain.use_cases.game

import ru.kheynov.api.v1.responses.InfoDetails
import ru.kheynov.domain.entities.UserInfo
import ru.kheynov.utils.GameRepositories

class GetGameInfoUseCase(
    gameRepositories: GameRepositories,
) {
    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    sealed interface Result {
        data class Successful(val info: InfoDetails) : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        if (roomsRepository.getRoomByName(roomName) == null) return Result.RoomNotExists
        val users = gameRepository.getUsersInRoom(roomName)
        if (users.find { it.userId == userId } == null) return Result.Forbidden
        val info = InfoDetails(
            roomName,
            users = users.map { UserInfo(it.userId, it.username) },
            gameRepository.getUsersRecipient(roomName, userId)
        )
        return Result.Successful(info)
    }
}