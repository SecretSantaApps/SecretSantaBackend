package ru.kheynov.domain.use_cases.game

import ru.kheynov.di.ServiceLocator
import ru.kheynov.utils.GameRepositories
import ru.kheynov.utils.GiftDispenser

class StartGameUseCase(
    gameRepositories: GameRepositories,
    private val giftDispenser: GiftDispenser = ServiceLocator.giftDispenser,
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
        object GameAlreadyStarted : Result
        object NotEnoughPlayers : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomByName(roomName) ?: return Result.RoomNotFound
        if (room.ownerId != userId) return Result.Forbidden
        if (room.gameStarted) return Result.GameAlreadyStarted

        val users = gameRepository.getUsersInRoom(roomName)

        if (users.size < 3) return Result.NotEnoughPlayers
        println(users.toString())
        val resultRelations = giftDispenser.getRandomDistribution(users = users.map { it.userId })

        resultRelations.forEach {
            if (!gameRepository.setRecipient(roomName, it.first, it.second)) return Result.Failed
        }
        gameRepository.setGameState(roomName, true)
        return Result.Successful
    }
}