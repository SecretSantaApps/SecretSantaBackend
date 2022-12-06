package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.UserAuth
import ru.kheynov.domain.entities.UserDTO
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

    suspend operator fun invoke(user: UserAuth): Result {
        if (usersRepository.getUserByID(user.userId) != null) return Result.UserAlreadyExists
        println("User id: ${user.displayName}")
        val resUser = UserDTO.User(
            userId = user.userId,
            username = if (user.displayName.isNullOrEmpty()) "Guest-${getRandomUsername()}" else user.displayName
        )

        return if (usersRepository.registerUser(resUser)) Result.Successful else Result.Failed
    }
}