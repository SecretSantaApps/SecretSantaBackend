package ru.kheynov.domain.use_cases.users

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.repositories.UsersRepository

class UpdateUserUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(
        userId: String,
        name: String?,
        address: String?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        return if (usersRepository.updateUserByID(
                userId,
                name = name,
                address = address,
            )
        ) Result.Successful else Result.Failed
    }
}