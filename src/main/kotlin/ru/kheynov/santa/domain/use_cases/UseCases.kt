package ru.kheynov.santa.domain.use_cases

import ru.kheynov.santa.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.santa.domain.use_cases.users.GetUserDetailsUseCase
import ru.kheynov.santa.domain.use_cases.users.UpdateUserUseCase
import ru.kheynov.santa.domain.use_cases.users.auth.LoginViaEmailUseCase
import ru.kheynov.santa.domain.use_cases.users.auth.RefreshTokenUseCase
import ru.kheynov.santa.domain.use_cases.users.auth.SignUpViaEmailUseCase
import ru.kheynov.santa.domain.use_cases.game.*
import ru.kheynov.santa.domain.use_cases.rooms.*
import ru.kheynov.santa.domain.use_cases.utils.GetAvatarsList

class UseCases {
    val createRoomUseCase = CreateRoomUseCase()
    val deleteRoomUseCase = DeleteRoomUseCase()
    val getRoomDetailsUseCase = GetRoomDetailsUseCase()
    val updateRoomUseCase = UpdateRoomUseCase()
    val getUserRoomsUseCase = GetUserRoomsUseCase()


    val signUpViaEmailUseCase = SignUpViaEmailUseCase()
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