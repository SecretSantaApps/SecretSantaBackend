package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.repositories.UsersRepository

class UpdateUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(
        userId: String,
        name: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        return if (usersRepository.updateUserByID(
                userId,
                name = name
            )
        ) Result.Successful else Result.Failed
    }
}