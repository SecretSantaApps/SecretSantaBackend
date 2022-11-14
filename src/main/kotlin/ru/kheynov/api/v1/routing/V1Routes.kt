package ru.kheynov.api.v1.routing

import io.ktor.server.routing.*
import ru.kheynov.di.ServiceLocator

fun Route.v1Routes() {
    route("v1") {
        configureUserRoutes(ServiceLocator.useCases)
        configureRoomsRoutes(ServiceLocator.useCases)
    }
}