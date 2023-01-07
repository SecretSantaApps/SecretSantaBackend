package ru.kheynov.domain.use_cases.users

import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.TokenClaim
import ru.kheynov.security.jwt.token.TokenConfig
import ru.kheynov.security.jwt.token.TokenPair
import ru.kheynov.security.jwt.token.TokenService

class LoginViaEmailUseCase(
    private val usersRepository: UsersRepository,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig,
) {
    sealed interface Result {
        data class Success(val tokenPair: TokenPair) : Result
        object UserNotFound : Result
        object Failed : Result
    }

    suspend operator fun invoke(email: String, password: String): Result {
        val user = usersRepository.getUserByEmail(email) ?: return Result.UserNotFound
        val passwordVerificationResult = hashingService.verify(password, user.passwordHash ?: return Result.Failed)
        val tokenPair = tokenService.generateTokenPair(tokenConfig, TokenClaim("userId", user.userId))

        val result = usersRepository.updateUserRefreshToken(
            userId = user.userId,
            refreshToken = tokenPair.refreshToken.token,
            refreshTokenExpiration = tokenPair.refreshToken.expiresAt
        )

        if (passwordVerificationResult.verified && result) {
            return Result.Success(tokenPair)
        }
        return Result.Failed
    }
}