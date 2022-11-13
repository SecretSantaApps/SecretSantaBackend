package ru.kheynov.domain.use_cases.rooms

import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import java.time.LocalDate

class CreateRoomUseCase(
    private val usersRepository: UsersRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object UserNotExists : Result
        object Failed : Result
    }

    suspend operator fun invoke(
        userId: String,
        roomName: String,
        ownerId: String,
        password: String?,
        date: LocalDate?,
        maxPrice: Int?,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val room = Room(
            password = password,
            name = roomName,
            date = date,
            ownerId = ownerId,
            maxPrice = maxPrice
        )
        return if (roomsRepository.createRoom(room)) Result.Successful else Result.Failed
    }
}