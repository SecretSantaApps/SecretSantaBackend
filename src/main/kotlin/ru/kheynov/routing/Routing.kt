package ru.kheynov.routing

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    println("PORT: ${System.getenv("SERVER_PORT")}")
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
