package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.getRandomUsername

class RegisterUserUseCase(
    private val usersRepository: UsersRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserAlreadyExists : Result
    }

    suspend operator fun invoke(user: User): Result {
        var userCopy = user
        if (usersRepository.getUserByID(user.userId) != null) return Result.UserAlreadyExists
        if (user.username.isEmpty()) {
            userCopy = userCopy.copy(username = "Guest-${getRandomUsername()}")
        }
        return if (usersRepository.registerUser(userCopy)) Result.Successful else Result.Failed
    }
}