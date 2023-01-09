package ru.kheynov.api.v1.routing
//
//import io.ktor.http.*
//import io.ktor.server.application.*
//import io.ktor.server.auth.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import ru.kheynov.api.v1.requests.game.KickUserRequest
//import ru.kheynov.domain.entities.UserAuth
//import ru.kheynov.domain.use_cases.UseCases
//import ru.kheynov.domain.use_cases.game.*
//import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
//
//fun Route.configureGameRoutes(
//    useCases: UseCases,
//) {
//    route("/game") {
//        authenticate(FIREBASE_AUTH) {
//            post("/join") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@post
//                }
//                val id = call.request.queryParameters["id"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
//                    return@post
//                }
//                val password = call.request.queryParameters["pass"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong password format")
//                    return@post
//                }
//
//                val res = useCases.joinRoomUseCase(
//                    userId = user.userId, roomId = id, password = password
//                )
//                when (res) {
//                    JoinRoomUseCase.Result.Failed -> {
//                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.Forbidden -> {
//                        call.respond(HttpStatusCode.Forbidden)
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.GameAlreadyStarted -> {
//                        call.respond(HttpStatusCode.Conflict, "Game already started")
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.RoomNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK)
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.UserNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@post
//                    }
//
//                    JoinRoomUseCase.Result.UserAlreadyInRoom -> {
//                        call.respond(HttpStatusCode.Conflict, "User already in room")
//                        return@post
//                    }
//                }
//            }
//        }
//        authenticate(FIREBASE_AUTH) {
//            post("/leave") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@post
//                }
//                val id = call.request.queryParameters["id"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
//                    return@post
//                }
//                val res = useCases.leaveRoomUseCase(
//                    userId = user.userId, roomId = id
//                )
//                when (res) {
//                    LeaveRoomUseCase.Result.Failed -> {
//                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
//                        return@post
//                    }
//
//                    LeaveRoomUseCase.Result.GameAlreadyStarted -> {
//                        call.respond(HttpStatusCode.Conflict, "Game already started")
//                        return@post
//                    }
//
//                    LeaveRoomUseCase.Result.RoomNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@post
//                    }
//
//                    LeaveRoomUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK)
//                        return@post
//                    }
//
//                    LeaveRoomUseCase.Result.UserNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@post
//                    }
//
//                    LeaveRoomUseCase.Result.UserNotInRoom -> {
//                        call.respond(HttpStatusCode.Forbidden, "User not in the room")
//                        return@post
//                    }
//                }
//            }
//        }
//        authenticate(FIREBASE_AUTH) {
//            post("/kick") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@post
//                }
//                val request = call.receiveNullable<KickUserRequest>() ?: run {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@post
//                }
//                val res = useCases.kickUserUseCase(
//                    selfId = user.userId,
//                    userId = request.userId,
//                    roomId = request.roomId,
//                )
//                when (res) {
//                    KickUserUseCase.Result.Failed -> {
//                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.Forbidden -> {
//                        call.respond(HttpStatusCode.Forbidden)
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.GameAlreadyStarted -> {
//                        call.respond(HttpStatusCode.Conflict, "Game already started")
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.RoomNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK)
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.UserNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.UserNotInRoom -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not in the room")
//                        return@post
//                    }
//
//                    KickUserUseCase.Result.NotAllowed -> {
//                        call.respond(HttpStatusCode.BadRequest, "You should use /leave instead of /kick")
//                        return@post
//                    }
//                }
//            }
//        }
//        authenticate(FIREBASE_AUTH) {
//            post("/start") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@post
//                }
//                val id = call.request.queryParameters["id"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
//                    return@post
//                }
//                val res = useCases.startGameUseCase(
//                    userId = user.userId, roomId = id
//                )
//
//                when (res) {
//                    StartGameUseCase.Result.Failed -> {
//                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.Forbidden -> {
//                        call.respond(HttpStatusCode.Forbidden)
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.GameAlreadyStarted -> {
//                        call.respond(HttpStatusCode.Conflict, "Game already started")
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.RoomNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.UserNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.NotEnoughPlayers -> {
//                        call.respond(HttpStatusCode.BadRequest, "Not enough users to start playing")
//                        return@post
//                    }
//
//                    StartGameUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK)
//                        return@post
//                    }
//                }
//            }
//        }
//        authenticate(FIREBASE_AUTH) {
//            post("/stop") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@post
//                }
//                val id = call.request.queryParameters["id"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
//                    return@post
//                }
//                val res = useCases.stopGameUseCase(
//                    userId = user.userId, roomId = id
//                )
//                when (res) {
//                    StopGameUseCase.Result.Failed -> {
//                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
//                        return@post
//                    }
//
//                    StopGameUseCase.Result.Forbidden -> {
//                        call.respond(HttpStatusCode.Forbidden)
//                        return@post
//                    }
//
//                    StopGameUseCase.Result.GameAlreadyStopped -> {
//                        call.respond(HttpStatusCode.Conflict, "Game already stopped")
//                        return@post
//                    }
//
//                    StopGameUseCase.Result.RoomNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@post
//                    }
//
//                    StopGameUseCase.Result.UserNotFound -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@post
//                    }
//
//                    StopGameUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK)
//                        return@post
//                    }
//                }
//            }
//        }
//        authenticate(FIREBASE_AUTH) {
//            get("/info") {
//                val user = call.principal<UserAuth>() ?: run {
//                    call.respond(HttpStatusCode.Unauthorized)
//                    return@get
//                }
//                val id = call.request.queryParameters["id"] ?: run {
//                    call.respond(HttpStatusCode.BadRequest, "Wrong room name")
//                    return@get
//                }
//                val res = useCases.getGameInfoUseCase(
//                    userId = user.userId, roomId = id
//                )
//
//                when (res) {
//                    GetGameInfoUseCase.Result.Forbidden -> {
//                        call.respond(HttpStatusCode.Forbidden)
//                        return@get
//                    }
//
//                    GetGameInfoUseCase.Result.RoomNotExists -> {
//                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
//                        return@get
//                    }
//
//                    GetGameInfoUseCase.Result.UserNotExists -> {
//                        call.respond(HttpStatusCode.BadRequest, "User not exists")
//                        return@get
//                    }
//
//                    is GetGameInfoUseCase.Result.Successful -> {
//                        call.respond(HttpStatusCode.OK, res.info)
//                        return@get
//                    }
//                }
//            }
//        }
//    }
//}
