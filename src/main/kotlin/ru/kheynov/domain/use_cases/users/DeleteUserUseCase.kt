package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.repositories.UsersRepository

class DeleteUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(userId: String): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        return if (usersRepository.deleteUserByID(userId)) Result.Successful else Result.Failed
    }
}