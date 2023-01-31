package ru.kheynov.domain.use_cases.game

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class JoinRoomUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val roomsRepository: RoomsRepository by inject()
    private val gameRepository: GameRepository by inject()

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object Forbidden : Result
        object RoomNotFound : Result
        object UserNotFound : Result
        object GameAlreadyStarted : Result
        object UserAlreadyInRoom : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomId: String,
        password: String?,
        wishlist: String?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotFound
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotFound
        if (gameRepository.checkUserInRoom(roomId, userId)) return Result.UserAlreadyInRoom
        if (room.gameStarted) return Result.GameAlreadyStarted
        return if (room.password == password) {
            if (gameRepository.addToRoom(room.id, userId, wishlist)) Result.Successful
            else Result.Failed
        } else Result.Forbidden
    }
}