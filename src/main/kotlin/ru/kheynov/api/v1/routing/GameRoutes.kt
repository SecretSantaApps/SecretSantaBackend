package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.game.JoinRoomRequest
import ru.kheynov.api.v1.requests.game.KickUserRequest
import ru.kheynov.api.v1.requests.game.LeaveRoomRequest
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.game.JoinRoomUseCase
import ru.kheynov.domain.use_cases.game.KickUserUseCase
import ru.kheynov.domain.use_cases.game.LeaveRoomUseCase
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
                        userId = user.userId,
                        roomName = request.roomName,
                        password = request.password
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
                        userId = user.userId,
                        roomName = request.roomName
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
                    when(res){
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
                    TODO()
                }
            }
            authenticate(FIREBASE_AUTH) {
                post("/stop") {
                    TODO()
                }
            }
        }
    }
}
