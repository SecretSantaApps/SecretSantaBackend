package ru.kheynov

import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.kheynov.data.rooms.MongoRoomsRepositoryImpl
import ru.kheynov.data.user.MongoUserRepositoryImpl
import ru.kheynov.plugins.configureHTTP
import ru.kheynov.plugins.configureMonitoring
import ru.kheynov.plugins.configureSecurity
import ru.kheynov.plugins.configureSerialization
import ru.kheynov.routing.configureRouting
import ru.kheynov.security.hashing.SHA256HashingService
import ru.kheynov.security.token.JwtTokenService
import ru.kheynov.security.token.TokenConfig

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    println(System.getenv("MONGO_CONNECTION_STRING"))
    val db = KMongo.createClient(
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

    configureSecurity(tokenConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(
        userRepository = userRepository,
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig,
        roomsRepository = roomsRepository
    )
}
