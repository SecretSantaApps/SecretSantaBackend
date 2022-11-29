package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.users.CreateUserRequest
import ru.kheynov.api.v1.requests.users.GetUserDetailsRequest
import ru.kheynov.api.v1.requests.users.UpdateUserRequest
import ru.kheynov.api.v1.responses.UserName
import ru.kheynov.domain.entities.UserAuth
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.rooms.GetUserRoomsUseCase
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.GetUserDetailsUseCase
import ru.kheynov.domain.use_cases.users.RegisterUserUseCase
import ru.kheynov.domain.use_cases.users.UpdateUserUseCase
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH


fun Route.configureUserRoutes(
    useCases: UseCases,
) {
    route("/user") {
        authenticate(FIREBASE_AUTH) {
            post { //Register user
                val userInfo = call.receiveNullable<CreateUserRequest>()
                val userAuth = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }
                val user = UserAuth(
                    userAuth.userId,
                    if (userInfo?.username.isNullOrBlank()) userAuth.displayName else userInfo?.username
                )

                when (useCases.registerUserUseCase(user)) {
                    RegisterUserUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@post
                    }

                    RegisterUserUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@post
                    }

                    RegisterUserUseCase.Result.UserAlreadyExists -> {
                        call.respond(HttpStatusCode.Conflict, "User already exists")
                        return@post
                    }
                }
            }
        }
        authenticate(FIREBASE_AUTH) { //get user info
            get {
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val res = useCases.getUserDetailsUseCase(
                    userId = user.userId,
                    selfId = user.userId,
                    roomName = null
                )
                when (res) {
                    GetUserDetailsUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@get
                    }

                    GetUserDetailsUseCase.Result.RoomNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
                        return@get
                    }

                    is GetUserDetailsUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK, res.user)
                        return@get
                    }

                    GetUserDetailsUseCase.Result.UserNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@get
                    }
                }

            }
        }

        authenticate(FIREBASE_AUTH) {
            get("/name") {
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                call.respond(HttpStatusCode.OK, UserName(user.displayName ?: ""))
            }
        }

        authenticate(FIREBASE_AUTH) {
            get("/rooms") {
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                when (val res = useCases.getUserRoomsUseCase(userId = user.userId)) {
                    is GetUserRoomsUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK, res.rooms)
                        return@get
                    }

                    GetUserRoomsUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@get
                    }
                }
            }
        }

        authenticate(FIREBASE_AUTH) {
            get("/info") {
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                val request = call.receiveNullable<GetUserDetailsRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val res = useCases.getUserDetailsUseCase(
                    userId = request.userId ?: user.userId,
                    selfId = user.userId,
                    roomName = request.roomName
                )
                when (res) {
                    GetUserDetailsUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@get
                    }

                    is GetUserDetailsUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK, res.user)
                        return@get
                    }

                    GetUserDetailsUseCase.Result.UserNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@get
                    }

                    GetUserDetailsUseCase.Result.RoomNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, "Room not exists")
                        return@get
                    }
                }
            }
        }
        authenticate(FIREBASE_AUTH) {
            delete {
                val userId = call.principal<UserAuth>()?.userId.toString()
                when (useCases.deleteUserUseCase(userId)) {
                    DeleteUserUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@delete
                    }

                    DeleteUserUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@delete
                    }

                    DeleteUserUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@delete
                    }
                }
            }
        }
        authenticate(FIREBASE_AUTH) {
            get("/authenticate") {
                if (call.principal<UserAuth>() == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                call.respond(HttpStatusCode.OK)
            }
        }
        authenticate(FIREBASE_AUTH) {
            patch {
                val userId = call.principal<UserAuth>()?.userId ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@patch
                }
                val userUpdate = call.receiveNullable<UpdateUserRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }
                when (useCases.updateUserUseCase(userId, userUpdate.username)) {
                    UpdateUserUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        return@patch
                    }

                    UpdateUserUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@patch
                    }

                    UpdateUserUseCase.Result.UserNotExists -> {
                        call.respond(HttpStatusCode.BadRequest, "User not exists")
                        return@patch
                    }
                }

            }
        }
    }
}