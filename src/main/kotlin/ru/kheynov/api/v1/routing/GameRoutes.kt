package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.game.*
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.game.*
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
import ru.kheynov.security.firebase.auth.UserAuth

fun Route.configureGameRoutes(
    useCases: UseCases,
) {
    authenticate {
        route("/game") {
            authenticate(FIREBASE_AUTH) {
                post("/join") {
                    val user = call.principal<UserAuth>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val request = call.receiveNullable<JoinRoomRequest>() ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val res = useCases.joinRoomUseCase(
                        userId = user.userId, roomName = request.roomName, password = request.password
                    )
                    when (res) {
                        JoinRoomUseCase.Result.Failed -> TODO()
                        JoinRoomUseCase.Result.Forbidden -> TODO()
                        JoinRoomUseCase.Result.GameAlreadyStarted -> TODO()
                        JoinRoomUseCase.Result.RoomNotFound -> TODO()
                        JoinRoomUseCase.Result.Successful -> TODO()
                        JoinRoomUseCase.Result.UserNotFound -> TODO()
                    }
                }
            }
            authenticate(FIREBASE_AUTH) {
                post("/leave") {
                    val user = call.principal<UserAuth>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val request = call.receiveNullable<LeaveRoomRequest>() ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val res = useCases.leaveRoomUseCase(
                        userId = user.userId, roomName = request.roomName
                    )
                    when (res) {
                        LeaveRoomUseCase.Result.Failed -> TODO()
                        LeaveRoomUseCase.Result.GameAlreadyStarted -> TODO()
                        LeaveRoomUseCase.Result.RoomNotFound -> TODO()
                        LeaveRoomUseCase.Result.Successful -> TODO()
                        LeaveRoomUseCase.Result.UserNotFound -> TODO()
                        LeaveRoomUseCase.Result.UserNotInRoom -> TODO()
                    }
                }
            }
            authenticate(FIREBASE_AUTH) {
                post("/kick") {
                    val user = call.principal<UserAuth>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val request = call.receiveNullable<KickUserRequest>() ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val res = useCases.kickUserUseCase(
                        selfId = user.userId,
                        userId = request.userId,
                        roomName = request.roomName,
                    )
                    when (res) {
                        KickUserUseCase.Result.Failed -> TODO()
                        KickUserUseCase.Result.Forbidden -> TODO()
                        KickUserUseCase.Result.GameAlreadyStarted -> TODO()
                        KickUserUseCase.Result.RoomNotFound -> TODO()
                        KickUserUseCase.Result.Successful -> TODO()
                        KickUserUseCase.Result.UserNotFound -> TODO()
                        KickUserUseCase.Result.UserNotInRoom -> TODO()
                    }
                }
            }
            authenticate(FIREBASE_AUTH) {
                post("/start") {
                    val user = call.principal<UserAuth>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val request = call.receiveNullable<StartGameRequest>() ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val res = useCases.startGameUseCase(
                        userId = user.userId, roomName = request.roomName
                    )

                    when (res) {
                        StartGameUseCase.Result.Failed -> {
                            call.respond(HttpStatusCode.BadRequest, "Something went wrong")
                            return@post
                        }

                        StartGameUseCase.Result.Forbidden -> {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                        StartGameUseCase.Result.GameAlreadyStarted -> {
                            call.respond(HttpStatusCode.Conflict, "Game already started")
                            return@post
                        }

                        StartGameUseCase.Result.RoomNotFound -> {
                            call.respond(HttpStatusCode.BadRequest, "Room not found")
                            return@post
                        }

                        StartGameUseCase.Result.Successful -> {
                            call.respond(HttpStatusCode.OK)
                            return@post
                        }

                        StartGameUseCase.Result.UserNotFound -> {
                            call.respond(HttpStatusCode.BadRequest, "User not found")
                            return@post
                        }
                    }
                }
            }
            authenticate(FIREBASE_AUTH) {
                post("/stop") {
                    val user = call.principal<UserAuth>() ?: run {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val request = call.receiveNullable<StopGameRequest>() ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val res = useCases.stopGameUseCase(
                        userId = user.userId,
                        roomName = request.roomName
                    )
                    when (res) {
                        StopGameUseCase.Result.Failed -> {
                            call.respond(HttpStatusCode.BadRequest, "Something went wrong")
                            return@post
                        }

                        StopGameUseCase.Result.Forbidden -> {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                        StopGameUseCase.Result.GameAlreadyStopped -> {
                            call.respond(HttpStatusCode.Conflict, "Game already stopped")
                            return@post
                        }

                        StopGameUseCase.Result.RoomNotFound -> {
                            call.respond(HttpStatusCode.BadRequest, "Room not found")
                            return@post
                        }

                        StopGameUseCase.Result.Successful -> {
                            call.respond(HttpStatusCode.OK)
                            return@post
                        }

                        StopGameUseCase.Result.UserNotFound -> {
                            call.respond(HttpStatusCode.BadRequest, "User not found")
                            return@post
                        }
                    }
                }
            }
        }
    }
}
