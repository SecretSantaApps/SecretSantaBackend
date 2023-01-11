package ru.kheynov.domain.use_cases.rooms

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.entities.RoomDTO
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

class GetUserRoomsUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val roomsRepository: RoomsRepository by inject()

    sealed interface Result {
        data class Successful(val rooms: List<RoomDTO.RoomInfo>) : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(
        userId: String,
    ): Result {
        if (usersRepository.getUserByID(userId) == null) return Result.UserNotExists
        val rooms = roomsRepository.getUserRooms(userId)
        return Result.Successful(rooms)
    }
}