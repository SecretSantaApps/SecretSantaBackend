package ru.kheynov.domain.use_cases.game

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.entities.GameState
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.utils.GiftDispenser

class StartGameUseCase : KoinComponent {
    private val giftDispenser: GiftDispenser by inject()
    private val usersRepository: UsersRepository by inject()
    private val roomsRepository: RoomsRepository by inject()
    private val gameRepository: GameRepository by inject()

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object RoomNotFound : Result
        object Forbidden : Result
        object UserNotFound : Result
        object GameAlreadyStarted : Result
        object NotEnoughPlayers : Result
        object ActiveRequests : Result // есть активные запросы на присоединение к комнате
    }

    suspend operator fun invoke(
        userId: String,
        roomId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (room.ownerId != userId) return Result.Forbidden
        if (room.gameState == GameState.GAME_STARTED) return Result.GameAlreadyStarted

        val users = gameRepository.getUsersInRoom(roomId).toMutableList()

        if (users.find { it.accepted == false } != null) return Result.ActiveRequests

        if (!room.playableOwner) {
            users.removeIf { it.userId == room.ownerId }
        }

        if (users.size < 3) return Result.NotEnoughPlayers
        println(users.toString())
        val resultRelations = giftDispenser.getRandomDistribution(users = users.map { it.userId })

        resultRelations.forEach {
            if (!gameRepository.setRecipient(roomId, it.first, it.second)) return Result.Failed
        }
        gameRepository.setGameState(roomId, GameState.GAME_STARTED)
        return Result.Successful
    }
}