package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.User
import ru.kheynov.utils.GameRepositories

class GetUserDetailsUseCase(
    gameRepositories: GameRepositories,
) {
    sealed interface Result {
        data class Successful(val user: User) : Result
        object Failed : Result
        object UserNotFound : Result
        object RoomNotFound : Result
    }

    private val usersRepository = gameRepositories.first
    private val roomsRepository = gameRepositories.second
    private val gameRepository = gameRepositories.third
    suspend operator fun invoke(
        userId: String,
        selfId: String, //ID of requester
        roomName: String?,
    ): Result {
        val user = usersRepository.getUserByID(userId) ?: return Result.UserNotFound
        if (user.userId != selfId || roomName == null) return Result.Successful(user = user)
        if (roomsRepository.getRoomByName(roomName) == null) return Result.RoomNotFound
        val recipient = gameRepository.getUsersRecipient(roomName, selfId)
        val res = user.copy(recipient = recipient)
        return Result.Successful(res)
    }
}