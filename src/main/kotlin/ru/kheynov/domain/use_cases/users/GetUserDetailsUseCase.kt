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
    suspend operator fun invoke(
        userId: String,
    ): Result {
        val user = usersRepository.getUserByID(userId) ?: return Result.UserNotFound
        return Result.Successful(user = user)
    }
}