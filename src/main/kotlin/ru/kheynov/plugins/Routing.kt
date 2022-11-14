package ru.kheynov.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.routing.v1Routes

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("Secret Santa Server")
        }
        route("/api") {
            v1Routes()
        }
    }
}
