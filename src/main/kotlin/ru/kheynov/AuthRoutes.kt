package ru.kheynov

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils
import ru.kheynov.data.requests.AuthRequest
import ru.kheynov.data.responses.AuthResponse
import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UserRepository
import ru.kheynov.security.hashing.HashingService
import ru.kheynov.security.hashing.SaltedHash
import ru.kheynov.security.token.TokenClaim
import ru.kheynov.security.token.TokenConfig
import ru.kheynov.security.token.TokenService

fun Route.configureAuthRoutes(
    hashingService: HashingService,
    userRepository: UserRepository,
    tokenConfig: TokenConfig,
    tokenService: TokenService,
) {
    signUp(hashingService, userRepository)
    signIn(userRepository, hashingService, tokenService, tokenConfig)
    authenticate()
}

fun Route.signUp(
    hashingService: HashingService,
    userRepository: UserRepository,
) {
    post("/signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.BadRequest, "Password or Login are incorrect")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username, password = saltedHash.hash, salt = saltedHash.salt
        )

        val findUser = userRepository.getUserByUsername(user.username)
        if (findUser?.username == request.username) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
            return@post
        }

        val wasAcknowledge = userRepository.insertUser(user)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.Conflict, "Couldn't insert user into DB")
            return@post
        }

        call.respond(HttpStatusCode.OK, "You are now signed up!")
    }
}

fun Route.signIn(
    userRepository: UserRepository,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    post("/signin") {
        lateinit var request: AuthRequest
        try {
            request = call.receive()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userRepository.getUserByUsername(request.username)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Incorrect username or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password, saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt,
            )
        )
        if (!isValidPassword) {
            println(
                "request password: ${DigestUtils.sha256Hex(user.salt + request.password)}, required password: ${user.password}"
            )
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig, TokenClaim(
                name = "userId",
                value = user.id.toString(),
            )
        )
        call.respond(
            status = HttpStatusCode.OK, message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("/secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }

}