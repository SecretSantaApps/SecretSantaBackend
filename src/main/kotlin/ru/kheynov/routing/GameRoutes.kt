package ru.kheynov.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.requests.GenerateRelationsRequest
import ru.kheynov.data.requests.JoinRoomRequest
import ru.kheynov.data.requests.LeaveRoomRequest
import ru.kheynov.di.ServiceLocator
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UserRepository

fun Route.configureGameRoutes(
    userRepository: UserRepository = ServiceLocator.userRepository,
    roomsRepository: RoomsRepository = ServiceLocator.roomsRepository,
) {
    authenticate {
        route("/room") {
            joinRoom(userRepository, roomsRepository)
            leaveRoom(userRepository, roomsRepository)
        }
    }
}

fun Route.joinRoom(
    userRepository: UserRepository,
    roomsRepository: RoomsRepository,
) {
    post("join") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class).toString()
        val (roomName, password) = call.receive<JoinRoomRequest>()
        val room = roomsRepository.getRoomByName(roomName)
        if (userRepository.getUserByID(userId) == null) {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            return@post
        }
        if (room == null) {
            call.respond(HttpStatusCode.NoContent, "Room not found")
            return@post
        }
        if (room.password != password) {
            call.respond(HttpStatusCode.Forbidden, "Incorrect password")
            return@post
        }
        val isSuccessful = roomsRepository.addUserToRoom(userId, roomName = roomName)
        if (!isSuccessful) {
            call.respond(HttpStatusCode.NotAcceptable)
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.leaveRoom(
    userRepository: UserRepository,
    roomsRepository: RoomsRepository,
) {
    delete("leave") {
        val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class).toString()
        val (roomName) = call.receive<LeaveRoomRequest>()
        val room = roomsRepository.getRoomByName(roomName)
        if (userRepository.getUserByID(userId) == null) {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            return@delete
        }
        if (room == null) {
            call.respond(HttpStatusCode.NoContent, "Room not found")
            return@delete
        }
        if (!room.usersId.contains(userId)) {
            call.respond(HttpStatusCode.BadRequest, "User is not in the room")
            return@delete
        }
        val isSuccessful = roomsRepository.deleteUserFromRoom(userId, roomName)
        if (!isSuccessful) {
            call.respond(HttpStatusCode.NotAcceptable)
            return@delete
        }
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.relations(
    roomsRepository: RoomsRepository,
    userRepository: UserRepository,
) {
    route("/relations") {
        post {
            val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class).toString()
            val (roomName) = call.receive<GenerateRelationsRequest>()
            val room = roomsRepository.getRoomByName(roomName)
            if (userRepository.getUserByID(userId) == null) {
                call.respond(HttpStatusCode.Forbidden, "User not found")
                return@post
            }
            if (room == null) {
                call.respond(HttpStatusCode.NoContent, "Room not found")
                return@post
            }
            if (room.creatorId != userId) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }
            if (room.relations != null) {
                call.respond(HttpStatusCode.Conflict, "Relations already exists")
                return@post
            }
            val relations = roomsRepository.generateRelations(roomName)
            if (relations.isEmpty()) {
                call.respond(HttpStatusCode.InternalServerError)
                return@post
            }
            call.respond(HttpStatusCode.OK, relations[userId].toString())
        }
        get {
            val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class).toString()
            val (roomName) = call.receive<GenerateRelationsRequest>()
            val room = roomsRepository.getRoomByName(roomName)
            if (userRepository.getUserByID(userId) == null) {
                call.respond(HttpStatusCode.Forbidden, "User not found")
                return@get
            }
            if (room == null) {
                call.respond(HttpStatusCode.NoContent, "Room not found")
                return@get
            }

            if (!room.usersId.contains(userId)) {
                call.respond(HttpStatusCode.Forbidden, "User not in the room")
                return@get
            }

            val relations = roomsRepository.generateRelations(roomName)
            if (relations.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No relations yet")
                return@get
            }
            call.respond(HttpStatusCode.OK, relations[userId].toString())
        }
    }
}