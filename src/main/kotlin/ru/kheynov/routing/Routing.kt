package ru.kheynov.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.kheynov.domain.repositories.UserRepository
import ru.kheynov.security.hashing.HashingService
import ru.kheynov.security.token.TokenConfig
import ru.kheynov.security.token.TokenService

fun Application.configureRouting(
    userRepository: UserRepository,
    tokenService: TokenService,
    hashingService: HashingService,
    tokenConfig: TokenConfig,
) {
    routing {
        configureAuthRoutes(hashingService, userRepository, tokenConfig, tokenService)
        configureUserOperations(hashingService, userRepository)
    }
}
