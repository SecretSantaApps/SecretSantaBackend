package ru.kheynov.domain.use_cases.game

import ru.kheynov.utils.GameRepositories

class AddRecipientUseCase(
    gameRepositories: GameRepositories,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object RoomNotFound : Result
        object UserNotFound : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third

    suspend operator fun invoke(
        roomName: String,
        userId: String,
        recipientId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        return if (gameRepository.addRecipient(
                roomName = room.name,
                userId = userId,
                recipientId = recipientId,
            )
        ) Result.Successful
        else Result.Failed
    }
}