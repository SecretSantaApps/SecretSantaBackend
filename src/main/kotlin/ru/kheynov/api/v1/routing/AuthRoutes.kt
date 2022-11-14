package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.domain.entities.User
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.users.AuthenticateUserUseCase
import ru.kheynov.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.domain.use_cases.users.RegisterUserUseCase
import ru.kheynov.security.firebase.auth.FIREBASE_AUTH
import ru.kheynov.security.firebase.auth.UserAuth


fun Route.configureAuthRoutes(
    useCases: UseCases,
) {
    route("/user") {
        authenticate(FIREBASE_AUTH) {
            post { //Register user
                val userAuth = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }
                val user = User(
                    userAuth.userId, userAuth.displayName
                )

                when (useCases.registerUserUseCase(user)) {
                    RegisterUserUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.Conflict, "Something went wrong")
                        return@post
                    }

                    RegisterUserUseCase.Result.Successful -> {
                        call.respond(HttpStatusCode.OK)
                        return@post
                    }

                    RegisterUserUseCase.Result.UserAlreadyExists -> {
                        call.respond(HttpStatusCode.Conflict, "User Already Registered")
                        return@post
                    }
                }
            }
        }
        authenticate(FIREBASE_AUTH) { //authenticate
            get {
                val user = call.principal<UserAuth>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                if (useCases.authenticateUserUseCase(user.userId) == AuthenticateUserUseCase.Result.UserNotExists) {
                    call.respond(HttpStatusCode.Conflict, "User not exists")
                    return@get
                }
                val res = User(user.userId, user.displayName)
                call.respond(HttpStatusCode.OK, res)
            }
        }
        authenticate(FIREBASE_AUTH) {
            delete {
                val userId = call.principal<UserAuth>()?.userId.toString()
                when (useCases.deleteUserUseCase(userId)) {
                    DeleteUserUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.Conflict, "Something went wrong")
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
    }
}