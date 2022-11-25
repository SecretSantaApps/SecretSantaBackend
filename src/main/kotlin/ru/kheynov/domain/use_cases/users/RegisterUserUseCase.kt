package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UsersRepository

class RegisterUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserAlreadyExists : Result
    }

    suspend operator fun invoke(user: User): Result {
        if (usersRepository.getUserByID(user.userId) != null) return Result.UserAlreadyExists
        return if (usersRepository.registerUser(user)) Result.Successful else Result.Failed
    }
}