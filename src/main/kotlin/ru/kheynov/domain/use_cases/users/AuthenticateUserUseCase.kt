package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UsersRepository

class AuthenticateUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        data class Successful(val user: User) : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(userId: String): Result {
        val user = usersRepository.getUserByID(userId = userId) ?: return Result.UserNotExists
        return Result.Successful(user)
    }
}