package ru.kheynov.api.v1.routing

import io.ktor.server.routing.*

fun Route.v1Routes() {
    route("v1") {
        configureAuthRoutes()
    }
}