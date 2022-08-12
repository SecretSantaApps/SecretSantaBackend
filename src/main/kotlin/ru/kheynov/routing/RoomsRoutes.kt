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
import ru.kheynov.di.ServiceLocator
import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UserRepository

fun Route.configureRoomsRoutes(
    repository: RoomsRepository = ServiceLocator.roomsRepository,
    userRepository: UserRepository = ServiceLocator.userRepository,
) {
    authenticate {
        route("/room") {
            createRoom(repository, userRepository)
            deleteRoom(repository, userRepository)
            getRoomByName(repository)
        }
    }
}

fun Route.createRoom(
    roomsRepository: RoomsRepository,
    userRepository: UserRepository,
) {

    post {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class).toString()

        if (userRepository.getUserByID(userId) == null) {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            return@post
        }

        if (userId.isBlank()) {
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
            name = request.name, password = request.password, creatorId = userId, usersId = listOf(userId)
        )
        val isSuccessful = roomsRepository.createRoom(room)
        if (!isSuccessful) {
            call.respond(HttpStatusCode.InternalServerError, "Cannot write to DB")
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
}


fun Route.deleteRoom(
    repository: RoomsRepository,
    userRepository: UserRepository,
) {
    delete {
        val request: DeleteRoomRequest

        val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class).toString()


        try {
            request = call.receive()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        if (userRepository.getUserByID(userId) == null ||
            repository.getRoomByName(request.name)?.creatorId != userId
        ) {
            call.respond(HttpStatusCode.Forbidden)
            return@delete
        }

        val isSuccessful = repository.deleteRoomByName(request.name)
        if (isSuccessful) call.respond(HttpStatusCode.OK, "Room deleted")
        else call.respond(HttpStatusCode.NotAcceptable, "Cannot delete room")
        return@delete
    }
}

fun Route.getRoomByName(
    repository: RoomsRepository,
) {
    get {
        val name = call.request.queryParameters["lang"].toString()
        val room = repository.getRoomByName(name)
        if (room == null) {
            call.respond(HttpStatusCode.NoContent, "Room not found")
            return@get
        }
        call.respond(HttpStatusCode.OK, room)
    }
}