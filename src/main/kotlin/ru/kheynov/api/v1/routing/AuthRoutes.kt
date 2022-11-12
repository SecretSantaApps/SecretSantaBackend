package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.domain.repositories.UserRepository
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
import ru.kheynov.security.firebase.auth.UserAuth


fun Route.configureAuthRoutes(

) {
    route("/user") {

    }
}

fun Route.authenticate(
    userRepository: UserRepository,
) {
    authenticate(FIREBASE_AUTH) {
        get("/authenticate") {
            val userId = call.principal<UserAuth>()?.userId.toString()
            if (userRepository.getUserByID(userId) == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}