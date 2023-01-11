package ru.kheynov.domain.use_cases.game

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class KickUserUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val roomsRepository: RoomsRepository by inject()
    private val gameRepository: GameRepository by inject()

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object Forbidden : Result
        object UserNotInRoom : Result
        object RoomNotFound : Result
        object UserNotFound : Result
        object GameAlreadyStarted : Result
        object NotAllowed : Result
    }

    suspend operator fun invoke(
        selfId: String, //ID of user that requested deletion
        userId: String,
        roomId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        if (gameRepository.getUsersInRoom(roomId).find { it.userId == userId } == null) return Result.UserNotInRoom
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (room.ownerId != selfId) return Result.Forbidden
        if (room.gameStarted) return Result.GameAlreadyStarted
        if (selfId == userId) return Result.NotAllowed
        return if (gameRepository.deleteFromRoom(
                roomId = roomId,
                userId = userId,
            )
        ) Result.Successful else Result.Failed
    }
}