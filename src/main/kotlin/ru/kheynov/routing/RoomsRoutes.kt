package ru.kheynov.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.requests.CreateRoomRequest
import ru.kheynov.data.requests.DeleteRoomRequest
import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository

fun Route.configureRoomsRoutes(
    repository: RoomsRepository,
) {
    route("/room") {
        createRoom(repository)
        deleteRoom(repository)
    }
}

fun Route.createRoom(
    repository: RoomsRepository,
) {
    authenticate {
        post {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Bad client ID")
                return@post
            }

            val request: CreateRoomRequest
            try {
                request = call.receive()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val room = Room(
                name = request.name,
                password = request.password,
                creatorId = userId,
                usersId = listOf(userId)
            )
            val isSuccessful = repository.createRoom(room)
            if (!isSuccessful) {
                call.respond(HttpStatusCode.InternalServerError, "Cannot write to DB")
                return@post
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.deleteRoom(
    repository: RoomsRepository,
) {
    authenticate {
        delete {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val request: DeleteRoomRequest

            try {
                request = call.receive()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            if (userId != null) {
                val isSuccessful = repository.deleteRoomByName(request.name)
                if (isSuccessful) call.respond(HttpStatusCode.OK, "Room deleted")
                else call.respond(HttpStatusCode.NotAcceptable, "Cannot delete room")
                return@delete
            }
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}