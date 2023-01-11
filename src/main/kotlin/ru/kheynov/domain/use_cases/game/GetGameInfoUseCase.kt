package ru.kheynov.domain.use_cases.game

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.api.v1.responses.InfoDetails
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class GetGameInfoUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val roomsRepository: RoomsRepository by inject()
    private val gameRepository: GameRepository by inject()

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