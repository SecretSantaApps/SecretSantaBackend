package ru.kheynov.domain.use_cases.users.auth

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.*

class LoginViaEmailUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val hashingService: HashingService by inject()
    private val tokenService: TokenService by inject()
    private val tokenConfig: TokenConfig by inject()

    sealed interface Result {
        data class Success(val tokenPair: TokenPair) : Result
        object UserNotFound : Result
        object Failed : Result
    }

    suspend operator fun invoke(email: String, password: String): Result {
        val user = usersRepository.getUserByEmail(email) ?: return Result.UserNotFound
        val passwordVerificationResult = hashingService.verify(password, user.passwordHash ?: return Result.Failed)
        val tokenPair = tokenService.generateTokenPair(tokenConfig, TokenClaim("userId", user.userId))

        val createRefreshTokenResult = usersRepository.createRefreshToken(
            userId = user.userId,
            refreshToken = RefreshToken(
                token = tokenPair.refreshToken.token,
                expiresAt = tokenPair.refreshToken.expiresAt,
            )
        )

        if (passwordVerificationResult.verified && createRefreshTokenResult) {
            return Result.Success(tokenPair)
        }
        return Result.Failed
    }
}