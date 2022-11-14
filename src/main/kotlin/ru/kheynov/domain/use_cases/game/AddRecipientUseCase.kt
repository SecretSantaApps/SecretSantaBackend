package ru.kheynov.domain.use_cases.game

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class AddRecipientUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
    private val gameRepository: GameRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object RoomNotFound : Result
        object UserNotFound : Result
    }

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