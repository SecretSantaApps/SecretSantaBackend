package ru.kheynov.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.routing.v1Routes
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
import ru.kheynov.security.firebase.auth.UserAuth

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("Secret Santa Server")
        }
        route("/api") {
            v1Routes()
        }
        route("/testApi") {
            authenticate(FIREBASE_AUTH) {
                get("/login") {
                    val user: UserAuth =
                        call.principal() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                    call.respond("User is authenticated: $user")
                }
            }
        }
    }
}
