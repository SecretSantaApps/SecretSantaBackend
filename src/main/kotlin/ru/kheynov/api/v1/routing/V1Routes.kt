package ru.kheynov.api.v1.routing

import io.ktor.server.routing.*
import ru.kheynov.di.ServiceLocator

fun Route.v1Routes() {
    route("v1") {
        val useCases = ServiceLocator.useCases
        configureUserRoutes(useCases)
        configureRoomsRoutes(useCases)
        configureGameRoutes(useCases)
    }
}