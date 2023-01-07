package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.utils.getRandomUserID
import ru.kheynov.utils.getRandomUsername
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.TokenClaim
import ru.kheynov.security.jwt.token.TokenConfig
import ru.kheynov.security.jwt.token.TokenPair
import ru.kheynov.security.jwt.token.TokenService

class RegisterViaEmailUseCase(
    private val usersRepository: UsersRepository,
    private val tokenService: TokenService,
    private val hashingService: HashingService,
    private val tokenConfig: TokenConfig,
) {
    sealed interface Result {
        data class Successful(val tokenPair: TokenPair) : Result
        object Failed : Result
        object UserAlreadyExists : Result
    }

    suspend operator fun invoke(user: UserDTO.UserEmailRegister): Result {
        if (usersRepository.getUserByEmail(user.email) != null) return Result.UserAlreadyExists

        val userId = getRandomUserID()

        val tokenPair = tokenService.generateTokenPair(tokenConfig, TokenClaim("userId", userId))

        val resUser = UserDTO.User(
            userId = userId,
            username = user.username.ifEmpty { "Guest-${getRandomUsername()}" },
            email = user.email,
            passwordHash = hashingService.generateHash(user.password),
            authProvider = "local",
            refreshToken = tokenPair.refreshToken.token,
            refreshTokenExpiration = tokenPair.refreshToken.expiresAt
        )

        return if (usersRepository.registerUser(resUser)) Result.Successful(tokenPair) else Result.Failed
    }
}