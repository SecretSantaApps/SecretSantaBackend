package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kheynov.api.v1.requests.rooms.CreateRoomRequest
import ru.kheynov.api.v1.requests.rooms.UpdateRoomRequest
import ru.kheynov.domain.entities.RoomDTO.RoomUpdate
import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.game.AcceptUserUseCase
import ru.kheynov.domain.use_cases.game.JoinRoomUseCase
import ru.kheynov.domain.use_cases.rooms.*

private val json = Json { encodeDefaults = true }

fun Route.configureRoomsRoutes(
    useCases: UseCases,
    roomsRepository: RoomsRepository,
) {
    route("/room") {
        authenticate {
            get {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "No access token provided")
                    return@get
                }

                val roomId = call.request.queryParameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
                    return@get
                }

                when (val res = useCases.getRoomDetailsUseCase(userId, roomId)) {
                    GetRoomDetailsUseCase.Result.RoomNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
                        return@get
                    }

                    GetRoomDetailsUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@get
                    }

                    GetRoomDetailsUseCase.Result.Forbidden -> {
                        call.respond(HttpStatusCode.Forbidden)
                        return@get
                    }

                    is GetRoomDetailsUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK, res.room)
                        return@get
                    }
                }
            }
        }
        authenticate {
            post {//Create room
                val request = call.receiveNullable<CreateRoomRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "No access token provided")
                    return@post
                }
                val res = useCases.createRoomUseCase(
                    userId = userId,
                    roomName = request.name,
                    date = request.date,
                    maxPrice = request.maxPrice,
                    playableOwner = request.playableOwner,
                )
                when (res) {
                    CreateRoomUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@post
                    }

                    is CreateRoomUseCase.Result.Successful -> {
                        val joinRequest = useCases.joinRoomUseCase(
                            userId = userId,
                            roomId = res.room.id,
                            wishlist = request.wishList
                        )
                        val acceptRequest = useCases.acceptUserUseCase(
                            selfId = userId,
                            userId = userId,
                            roomId = res.room.id
                        )
                        if (joinRequest is JoinRoomUseCase.Result.Successful && acceptRequest is AcceptUserUseCase.Result.Successful) {
                            call.respond(HttpStatusCode.OK, res.room)
                        } else call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@post
                    }

                    CreateRoomUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@post
                    }
                }
            }
        }
        authenticate {
            delete {
                val roomId = call.request.queryParameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
                    return@delete
                }
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "No access token provided")
                    return@delete
                }
                when (useCases.deleteRoomUseCase(
                    userId = userId, roomId = roomId
                )) {
                    DeleteRoomUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@delete
                    }

                    DeleteRoomUseCase.Result.Forbidden -> {
                        call.respond(HttpStatusCode.Forbidden)
                        return@delete
                    }

                    DeleteRoomUseCase.Result.RoomNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
                        return@delete
                    }

                    DeleteRoomUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@delete
                    }

                    DeleteRoomUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User Not Exists")
                        return@delete
                    }
                }
            }
        }

        authenticate {
            patch {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    call.respond(HttpStatusCode.Unauthorized, "No access token provided")
                    return@patch
                }

                val roomId = call.request.queryParameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, "Wrong room id")
                    return@patch
                }
                val roomUpdate = call.receiveNullable<UpdateRoomRequest>()?.let {
                    RoomUpdate(
                        name = it.name,
                        date = it.date,
                        maxPrice = it.maxPrice,
                    )
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }

                when (useCases.updateRoomUseCase(
                    userId = userId,
                    roomId = roomId,
                    roomUpdate = roomUpdate
                )) {
                    UpdateRoomUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@patch
                    }

                    UpdateRoomUseCase.Result.Forbidden -> {
                        call.respond(HttpStatusCode.Forbidden)
                        return@patch
                    }

                    UpdateRoomUseCase.Result.RoomNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
                        return@patch
                    }

                    UpdateRoomUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@patch
                    }

                    UpdateRoomUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@patch
                    }
                }
            }
        }
        authenticate {
            webSocket {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    send(Frame.Text("No access token provided"))
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No access token provided"))
                    return@webSocket
                }
                val roomId = call.request.queryParameters["id"] ?: run {
                    send(Frame.Text("Wrong room id"))
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Wrong room id"))
                    return@webSocket
                }
                when (val rooms = useCases.getUserRoomsUseCase(userId)) {
                    is GetUserRoomsUseCase.Result.Successful -> {
                        rooms.rooms.find { it.id == roomId } ?: run {
                            send(Frame.Text("User not in the room"))
                            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "User not in the room"))
                            return@webSocket
                        }
                    }

                    GetUserRoomsUseCase.Result.UserNotExists -> {
                        send(Frame.Text("User not exists"))
                        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "User not exists"))
                        return@webSocket
                    }
                }
                roomsRepository.updates.collect {
                    send(Frame.Text(json.encodeToString(it.update)))
                }
            }
        }
    }
}
