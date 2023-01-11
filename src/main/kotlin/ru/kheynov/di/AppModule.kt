package ru.kheynov.di

import org.koin.dsl.module
import org.ktorm.database.Database
import ru.kheynov.data.repositories.game.PostgresGameRepository
import ru.kheynov.data.repositories.rooms.PostgresRoomsRepository
import ru.kheynov.data.repositories.users.PostgresUsersRepository
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.security.jwt.hashing.BcryptHashingService
import ru.kheynov.security.jwt.hashing.HashingService
import ru.kheynov.security.jwt.token.JwtTokenService
import ru.kheynov.security.jwt.token.TokenConfig
import ru.kheynov.security.jwt.token.TokenService
import ru.kheynov.utils.GiftDispenser
import ru.kheynov.utils.SimpleCycleGiftDispenser

val appModule = module {
    single {
        Database.connect(
            url = System.getenv("DATABASE_CONNECTION_STRING"),
            driver = "org.postgresql.Driver",
            user = System.getenv("POSTGRES_NAME"),
            password = System.getenv("POSTGRES_PASSWORD")
        )
    }

    single<UsersRepository> { PostgresUsersRepository(get()) }
    single<RoomsRepository> { PostgresRoomsRepository(get()) }
    single<GameRepository> { PostgresGameRepository(get()) }

    single {
        TokenConfig(
            issuer = System.getenv("JWT_ISSUER"),
            audience = System.getenv("JWT_AUDIENCE"),
            accessLifetime = System.getenv("JWT_ACCESS_LIFETIME").toLong(),
            refreshLifetime = System.getenv("JWT_REFRESH_LIFETIME").toLong(),
            secret = System.getenv("JWT_SECRET")
        )
    }

    single<TokenService> { JwtTokenService() }

    single<HashingService> { BcryptHashingService() }

    single<GiftDispenser> { SimpleCycleGiftDispenser() }

    single { UseCases() }


}