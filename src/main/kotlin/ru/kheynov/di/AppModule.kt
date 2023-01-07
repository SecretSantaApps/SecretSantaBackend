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
            issuer = System.getenv("jwt-issuer"),
            audience = System.getenv("jwt-issuer"),
            accessLifetime = System.getenv("jwt-access-lifetime").toLong(),
            refreshLifetime = System.getenv("jwt-refresh-lifetime").toLong(),
            secret = System.getenv("jwt-secret")
        )
    }

    single<TokenService> { JwtTokenService() }

    single<GiftDispenser> { SimpleCycleGiftDispenser() }

    single { UseCases(get(), get(), get()) }


}