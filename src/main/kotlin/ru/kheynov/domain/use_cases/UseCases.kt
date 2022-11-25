package ru.kheynov.domain.use_cases

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.game.*
import ru.kheynov.domain.use_cases.rooms.*
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.GetUserDetailsUseCase
import ru.kheynov.domain.use_cases.users.RegisterUserUseCase
import ru.kheynov.domain.use_cases.users.UpdateUserUseCase

class UseCases(
    roomsRepository: RoomsRepository,
    usersRepository: UsersRepository,
    gameRepository: GameRepository,
) {
    val createRoomUseCase = CreateRoomUseCase(usersRepository, roomsRepository)
    val deleteRoomUseCase = DeleteRoomUseCase(usersRepository, roomsRepository)
    val getRoomDetailsUseCase = GetRoomDetailsUseCase(usersRepository, roomsRepository)
    val updateRoomUseCase = UpdateRoomUseCase(usersRepository, roomsRepository)
    val getUserRoomsUseCase = GetUserRoomsUseCase(usersRepository, roomsRepository)


    val registerUserUseCase = RegisterUserUseCase(usersRepository)
    val deleteUserUseCase = DeleteUserUseCase(usersRepository)
    val updateUserUseCase = UpdateUserUseCase(usersRepository)


    private val gameRepositories =
        Triple(usersRepository, roomsRepository, gameRepository)

    val getUserDetailsUseCase = GetUserDetailsUseCase(gameRepositories)
    val joinRoomUseCase = JoinRoomUseCase(gameRepositories)
    val leaveRoomUseCase = LeaveRoomUseCase(gameRepositories)
    val kickUserUseCase = KickUserUseCase(gameRepositories)
    val startGameUseCase = StartGameUseCase(gameRepositories)
    val stopGameUseCase = StopGameUseCase(gameRepositories)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRepositories)
}