package ru.kheynov.domain.use_cases.users.auth

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.security.jwt.token.TokenClaim
import ru.kheynov.security.jwt.token.TokenConfig
import ru.kheynov.security.jwt.token.TokenPair
import ru.kheynov.security.jwt.token.TokenService

class RefreshTokenUseCase : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    private val tokenService: TokenService by inject()
    private val tokenConfig: TokenConfig by inject()

    sealed interface Result {
        data class Success(val tokenPair: TokenPair) : Result
        object InvalidRefreshToken : Result
        object RefreshTokenExpired : Result
        object Forbidden : Result
        object Failed : Result
    }

    suspend operator fun invoke(
        userId: String,
        oldRefreshToken: ru.kheynov.security.jwt.token.RefreshToken,
    ): Result {
        val refreshToken = usersRepository.getRefreshTokenByUserId(userId) ?: return Result.InvalidRefreshToken
        if (refreshToken.token != oldRefreshToken.token) return Result.Forbidden
        if (refreshToken.expiresAt < System.currentTimeMillis()) return Result.RefreshTokenExpired

        val newTokenPair = tokenService.generateTokenPair(tokenConfig, TokenClaim("userId", userId))
        val updateRefreshTokenResult = usersRepository.updateUserRefreshToken(
            oldRefreshToken = oldRefreshToken.token,
            newRefreshToken = newTokenPair.refreshToken.token,
            refreshTokenExpiration = newTokenPair.refreshToken.expiresAt,
        )
        return if (updateRefreshTokenResult) Result.Success(newTokenPair) else Result.Failed
    }
}