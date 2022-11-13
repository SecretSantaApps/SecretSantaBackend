package ru.kheynov.domain.use_cases

import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.rooms.CreateRoomUseCase
import ru.kheynov.domain.use_cases.rooms.DeleteRoomUseCase
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.RegisterUserUseCase

class UseCases(
    roomsRepository: RoomsRepository,
    usersRepository: UsersRepository,
) {
    val createRoomUseCase = CreateRoomUseCase(usersRepository, roomsRepository)
    val deleteRoomUseCase = DeleteRoomUseCase(usersRepository, roomsRepository)

    val registerUserUseCase = RegisterUserUseCase(usersRepository)
    val deleteUserUseCase = DeleteUserUseCase(usersRepository)
}