package ru.kheynov.domain.use_cases

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.game.*
import ru.kheynov.domain.use_cases.rooms.CreateRoomUseCase
import ru.kheynov.domain.use_cases.rooms.DeleteRoomUseCase
import ru.kheynov.domain.use_cases.rooms.GetRoomDetailsUseCase
import ru.kheynov.domain.use_cases.rooms.UpdateRoomUseCase
import ru.kheynov.domain.use_cases.users.*

class UseCases(
    roomsRepository: RoomsRepository,
    usersRepository: UsersRepository,
    gameRepository: GameRepository,
) {
    val createRoomUseCase = CreateRoomUseCase(usersRepository, roomsRepository)
    val deleteRoomUseCase = DeleteRoomUseCase(usersRepository, roomsRepository)
    val getRoomDetailsUseCase = GetRoomDetailsUseCase(usersRepository, roomsRepository)
    val updateRoomUseCase = UpdateRoomUseCase(usersRepository, roomsRepository)


    val registerUserUseCase = RegisterUserUseCase(usersRepository)
    val deleteUserUseCase = DeleteUserUseCase(usersRepository)
    val authenticateUserUseCase = AuthenticateUserUseCase(usersRepository, gameRepository)
    val updateUserUseCase = UpdateUserUseCase(usersRepository)
    val getUserDetailsUseCase = GetUserDetailsUseCase(usersRepository, gameRepository)


    private val gameRepositories =
        Triple(usersRepository, roomsRepository, gameRepository)

    val joinRoomUseCase = JoinRoomUseCase(gameRepositories)
    val leaveRoomUseCase = LeaveRoomUseCase(gameRepositories)
    val kickUserUseCase = KickUserUseCase(gameRepositories)
    val startGameUseCase = StartGameUseCase(gameRepositories)
    val stopGameUseCase = StopGameUseCase(gameRepositories)
}