package ru.kheynov.domain.use_cases

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.game.*
import ru.kheynov.domain.use_cases.rooms.*
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.GetUserDetailsUseCase
import ru.kheynov.domain.use_cases.users.UpdateUserUseCase
import ru.kheynov.domain.use_cases.users.auth.LoginViaEmailUseCase
import ru.kheynov.domain.use_cases.users.auth.RefreshTokenUseCase
import ru.kheynov.domain.use_cases.users.auth.RegisterViaEmailUseCase
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.TokenConfig
import ru.kheynov.security.jwt.token.TokenService

class UseCases(
    roomsRepository: RoomsRepository,
    usersRepository: UsersRepository,
    gameRepository: GameRepository,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    hashingService: HashingService,
) {
    val createRoomUseCase = CreateRoomUseCase(usersRepository, roomsRepository)
    val deleteRoomUseCase = DeleteRoomUseCase(usersRepository, roomsRepository)
    val getRoomDetailsUseCase = GetRoomDetailsUseCase(usersRepository, roomsRepository)
    val updateRoomUseCase = UpdateRoomUseCase(usersRepository, roomsRepository)
    val getUserRoomsUseCase = GetUserRoomsUseCase(usersRepository, roomsRepository)


    val registerViaEmailUseCase = RegisterViaEmailUseCase(
        usersRepository = usersRepository,
        tokenService = tokenService,
        hashingService = hashingService,
        tokenConfig = tokenConfig,
    )
    val loginViaEmailUseCase = LoginViaEmailUseCase(
        usersRepository,
        hashingService,
        tokenService,
        tokenConfig,
    )

    val refreshTokenUseCase = RefreshTokenUseCase(
        usersRepository, tokenService, tokenConfig
    )

    val deleteUserUseCase = DeleteUserUseCase(usersRepository)
    val updateUserUseCase = UpdateUserUseCase(usersRepository)


    private val gameRepositories = Triple(usersRepository, roomsRepository, gameRepository)

    val getUserDetailsUseCase = GetUserDetailsUseCase(gameRepositories)
    val joinRoomUseCase = JoinRoomUseCase(gameRepositories)
    val leaveRoomUseCase = LeaveRoomUseCase(gameRepositories)
    val kickUserUseCase = KickUserUseCase(gameRepositories)
    val startGameUseCase = StartGameUseCase(gameRepositories)
    val stopGameUseCase = StopGameUseCase(gameRepositories)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRepositories)
}