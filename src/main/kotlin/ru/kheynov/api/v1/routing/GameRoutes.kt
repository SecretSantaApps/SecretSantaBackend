package ru.kheynov.api.v1.routing

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGameRoutes(

) {
    authenticate {
        route("/game") {

        }
    }
}
