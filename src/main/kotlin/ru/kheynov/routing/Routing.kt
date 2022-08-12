package ru.kheynov.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(){
    routing {
        configureAuthRoutes()
        configureUserOperations()
        configureRoomsRoutes()
        configureGameRoutes()
    }
}
