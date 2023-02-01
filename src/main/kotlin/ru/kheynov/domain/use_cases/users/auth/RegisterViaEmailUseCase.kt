package ru.kheynov.domain.use_cases.users.auth

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.*
import ru.kheynov.utils.getRandomUserID
import ru.kheynov.utils.getRandomUsername

class RegisterViaEmailUseCase : KoinComponent {

    private val usersRepository: UsersRepository by inject()
    private val tokenService: TokenService by inject()
    private val hashingService: HashingService by inject()
    private val tokenConfig: TokenConfig by inject()

    sealed interface Result {
        data class Successful(val tokenPair: TokenPair) : Result
        object Failed : Result
        object UserAlreadyExists : Result
        object AvatarNotFound : Result
    }

    suspend operator fun invoke(user: UserDTO.UserEmailRegister): Result {
        if (usersRepository.getUserByEmail(user.email) != null) return Result.UserAlreadyExists
        val userId = getRandomUserID()
        val tokenPair = tokenService.generateTokenPair(tokenConfig, TokenClaim("userId", userId))

        val avatar = usersRepository.getAvatarById(user.avatar) ?: return Result.AvatarNotFound

        val resUser = UserDTO.User(
            userId = userId,
            username = user.username.ifEmpty { "Guest-${getRandomUsername()}" },
            email = user.email,
            passwordHash = hashingService.generateHash(user.password),
            authProvider = "local",
            address = user.address,
            avatar = avatar,
        )
        val registerUserResult = usersRepository.registerUser(resUser)
        val createUserRefreshTokenResult = usersRepository.createRefreshToken(
            userId = userId, clientId = user.clientId, refreshToken = RefreshToken(
                token = tokenPair.refreshToken.token, expiresAt = tokenPair.refreshToken.expiresAt
            )
        )

        return if (registerUserResult && createUserRefreshTokenResult) Result.Successful(tokenPair) else Result.Failed
    }
}