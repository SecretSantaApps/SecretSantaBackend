package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.UsersRepository

class GetUserDetailsUseCase(
    private val usersRepository: UsersRepository,
    private val gameRepository: GameRepository,
) {
    sealed interface Result {
        data class Successful(val user: User) : Result
        object Failed : Result
        object UserNotFound : Result
    }

    suspend operator fun invoke(
        userId: String,
        selfId: String, //ID of requester
        roomName: String,
    ): Result {
        val user = usersRepository.getUserByID(userId) ?: return Result.UserNotFound
        if (user.userId != selfId) return Result.Successful(user = user)
        val recipient = gameRepository.getUsersRecipient(roomName, selfId) ?: return Result.Failed
        val res = user.copy(recipient = recipient)
        return Result.Successful(res)
    }
}