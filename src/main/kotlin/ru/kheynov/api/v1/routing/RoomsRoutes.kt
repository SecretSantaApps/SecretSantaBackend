package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.CreateRoomRequest
import ru.kheynov.api.v1.requests.DeleteRoomRequest
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.rooms.CreateRoomUseCase
import ru.kheynov.domain.use_cases.rooms.DeleteRoomUseCase
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
import ru.kheynov.security.firebase.auth.UserAuth

fun Route.configureRoomsRoutes(
    useCases: UseCases,
) {
    route("/room") {
        authenticate(FIREBASE_AUTH) {
            post {//Create room
                val request = call.receiveNullable<CreateRoomRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }
                val res = useCases.createRoomUseCase(
                    userId = user.userId,
                    roomName = request.name,
                    password = request.password,
                    date = null,
                    maxPrice = request.maxPrice,
                )
                when (res) {
                    CreateRoomUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.Conflict, "Something went wrong")
                        return@post
                    }

                    CreateRoomUseCase.Result.RoomAlreadyExists -> {
                        call.respond(HttpStatusCode.Conflict, "Room already exists")
                        return@post
                    }

                    CreateRoomUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@post
                    }

                    CreateRoomUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@post
                    }
                }
            }
            delete {
                val request = call.receiveNullable<DeleteRoomRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@delete
                }
                when (useCases.deleteRoomUseCase(user.userId, request.name)) {
                    DeleteRoomUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.Conflict, "Something went wrong")
                        return@delete
                    }

                    DeleteRoomUseCase.Result.Forbidden -> {
                        call.respond(HttpStatusCode.Forbidden)
                        return@delete
                    }

                    DeleteRoomUseCase.Result.RoomNotExists -> {
                        call.respond(HttpStatusCode.Conflict, "Room not exists")
                        return@delete
                    }

                    DeleteRoomUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@delete
                    }

                    DeleteRoomUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.Conflict, "User Not Exists")
                        return@delete
                    }
                }
            }
        }
    }
}
