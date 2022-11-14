package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.repositories.UsersRepository

class AuthenticateUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        object Successful : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(userId: String): Result {
        if (usersRepository.getUserByID(userId = userId) == null) return Result.UserNotExists
        return Result.Successful
    }
}