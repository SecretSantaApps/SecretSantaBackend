package ru.kheynov.domain.use_cases.game

import ru.kheynov.api.v1.responses.InfoDetails
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
        roomId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        val users = gameRepository.getUsersInRoom(roomId)
        println("Users: $users")
        if (users.find { it.userId == userId } == null) return Result.Forbidden
        val info = InfoDetails(
            roomId = room.id,
            roomName = room.name,
            ownerId = room.ownerId,
            password = if (userId == room.ownerId) room.password else null,
            date = room.date,
            maxPrice = room.maxPrice,
            users = users,
            recipient = gameRepository.getUsersRecipient(roomId, userId)
        )
        return Result.Successful(info)
    }
}