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
import ru.kheynov.data.responses.RoomInfoResponse
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
            relations(userRepository, roomsRepository)
            getUserRooms(userRepository, roomsRepository)
        }
    }
}

fun Route.getUserRooms(
    userRepository: UserRepository,
    roomsRepository: RoomsRepository,
) {
    get("/list") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.getClaim("userId", String::class).toString()
        if (userRepository.getUserByID(userId) == null) {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            return@get
        }
        val rooms = roomsRepository.getUserRooms(userId)

        val res = rooms.map { RoomInfoResponse(it.name, it.password, it.creatorId, it.usersId) }

        call.respond(HttpStatusCode.OK, res)
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
        var room = roomsRepository.getRoomByName(roomName)
        if (userRepository.getUserByID(userId) == null) {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            return@post
        }
        if (room == null) {
            call.respond(HttpStatusCode.NotFound, "Room not found")
            return@post
        }
        if (room.password != password) {
            call.respond(HttpStatusCode.Forbidden, "Incorrect password")
            return@post
        }
        if (roomsRepository.getRoomByName(roomName)?.usersId?.contains(userId) == true) {
            call.respond(HttpStatusCode.Conflict, "User already in the room")
            return@post
        }
        val isSuccessful = roomsRepository.addUserToRoom(userId, roomName = roomName)
        if (!isSuccessful) {
            call.respond(HttpStatusCode.NotAcceptable)
            return@post
        }
        roomsRepository.clearRelations(roomName)
        room = roomsRepository.getRoomByName(roomName)!!
        val response = RoomInfoResponse(
            name = room.name,
            password = room.password,
            creatorId = room.creatorId,
            usersId = room.usersId
        )
        call.respond(HttpStatusCode.OK, response)
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
            call.respond(HttpStatusCode.NotFound, "Room not found")
            return@delete
        }
        if (!room.usersId.contains(userId)) {
            call.respond(HttpStatusCode.BadRequest, "User is not in the room")
            return@delete
        }
        if (room.creatorId == userId) {
            call.respond(
                HttpStatusCode.BadRequest,
                "You cannot leave this room, because you are creator, you can only delete this room permanently"
            )
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
    userRepository: UserRepository,
    roomsRepository: RoomsRepository,
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
            if (room.usersId.size < 2) {
                call.respond(HttpStatusCode.NotAcceptable, "Not enough users to generate relations")
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
                call.respond(HttpStatusCode.NotFound, "Room not found")
                return@get
            }

            if (!room.usersId.contains(userId)) {
                call.respond(HttpStatusCode.Forbidden, "User not in the room")
                return@get
            }


            val relations = roomsRepository.getRoomByName(roomName)?.relations
            if (relations.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No relations yet")
                return@get
            }
            call.respond(HttpStatusCode.OK, relations[userId].toString())
        }
    }
}