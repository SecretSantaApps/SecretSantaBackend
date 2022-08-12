package ru.kheynov.di

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.kheynov.data.rooms.MongoRoomsRepositoryImpl
import ru.kheynov.data.user.MongoUserRepositoryImpl
import ru.kheynov.security.hashing.SHA256HashingService
import ru.kheynov.security.token.JwtTokenService
import ru.kheynov.security.token.TokenConfig

object ServiceLocator {
    private val db = KMongo.createClient(
        connectionString = System.getenv("MONGO_CONNECTION_STRING")
    ).coroutine.getDatabase("secret-santa")

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = System.getenv("JWT_ISSUER"),
        audience = System.getenv("JWT_AUDIENCE"),
        expiresIn = 1000L * 60L * 60L * 24L * 7L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    val userRepository = MongoUserRepositoryImpl(db)

    val roomsRepository = MongoRoomsRepositoryImpl(db)
}