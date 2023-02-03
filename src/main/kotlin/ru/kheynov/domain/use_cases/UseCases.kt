package ru.kheynov.domain.use_cases

import ru.kheynov.domain.use_cases.game.*
import ru.kheynov.domain.use_cases.rooms.*
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.GetUserDetailsUseCase
import ru.kheynov.domain.use_cases.users.UpdateUserUseCase
import ru.kheynov.domain.use_cases.users.auth.LoginViaEmailUseCase
import ru.kheynov.domain.use_cases.users.auth.RefreshTokenUseCase
import ru.kheynov.domain.use_cases.users.auth.RegisterViaEmailUseCase
import ru.kheynov.domain.use_cases.utils.GetAvatarsList

class UseCases {
    val createRoomUseCase = CreateRoomUseCase()
    val deleteRoomUseCase = DeleteRoomUseCase()
    val getRoomDetailsUseCase = GetRoomDetailsUseCase()
    val updateRoomUseCase = UpdateRoomUseCase()
    val getUserRoomsUseCase = GetUserRoomsUseCase()


    val registerViaEmailUseCase = RegisterViaEmailUseCase()
    val loginViaEmailUseCase = LoginViaEmailUseCase()
    val refreshTokenUseCase = RefreshTokenUseCase()

    val deleteUserUseCase = DeleteUserUseCase()
    val updateUserUseCase = UpdateUserUseCase()

    val getUserDetailsUseCase = GetUserDetailsUseCase()
    val joinRoomUseCase = JoinRoomUseCase()
    val leaveRoomUseCase = LeaveRoomUseCase()
    val kickUserUseCase = KickUserUseCase()
    val startGameUseCase = StartGameUseCase()
    val stopGameUseCase = StopGameUseCase()
    val getGameInfoUseCase = GetGameInfoUseCase()
    val acceptUserUseCase = AcceptUserUseCase()

    val getAvailableAvatarsUseCase = GetAvatarsList()
}