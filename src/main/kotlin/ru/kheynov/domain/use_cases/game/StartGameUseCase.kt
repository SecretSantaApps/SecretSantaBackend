package ru.kheynov.domain.use_cases.game

import ru.kheynov.di.ServiceLocator
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.utils.GiftDispenser

class StartGameUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
    private val gameRepository: GameRepository,
    private val giftDispenser: GiftDispenser = ServiceLocator.giftDispenser,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object RoomNotFound : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(
        roomName: String,
        userId: String,
    ): Result {
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        if (room.ownerId != userId) return Result.Forbidden
        val roomId = room.id ?: return Result.Failed
        val users = gameRepository.getUsersInRoom(roomId)
        val resultRelations = giftDispenser.getRandomDistribution(users = users.map { it.userId })
        resultRelations.forEach {
            if (!gameRepository.addRecipient(roomId, it.first, it.second)) return Result.Failed
        }
        return Result.Successful
    }
}