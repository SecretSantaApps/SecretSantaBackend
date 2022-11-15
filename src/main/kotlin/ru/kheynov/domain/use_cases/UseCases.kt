package ru.kheynov.domain.use_cases

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.game.AddRecipientUseCase
import ru.kheynov.domain.use_cases.game.JoinRoomUseCase
import ru.kheynov.domain.use_cases.rooms.CreateRoomUseCase
import ru.kheynov.domain.use_cases.rooms.DeleteRoomUseCase
import ru.kheynov.domain.use_cases.rooms.GetRoomDetailsUseCase
import ru.kheynov.domain.use_cases.rooms.UpdateRoomUseCase
import ru.kheynov.domain.use_cases.users.AuthenticateUserUseCase
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
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


    val registerUserUseCase = RegisterUserUseCase(usersRepository)
    val deleteUserUseCase = DeleteUserUseCase(usersRepository)
    val authenticateUserUseCase = AuthenticateUserUseCase(usersRepository)
    val updateUserUseCase = UpdateUserUseCase(usersRepository)


    private val gameRepositories =
        Triple(usersRepository, roomsRepository, gameRepository)

    val addRecipientUseCase = AddRecipientUseCase(gameRepositories)
    val joinRoomUseCase = JoinRoomUseCase(gameRepositories)
}