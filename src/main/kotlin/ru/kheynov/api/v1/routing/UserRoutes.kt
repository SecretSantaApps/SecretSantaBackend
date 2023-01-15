package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.users.auth.LoginViaEmailRequest
import ru.kheynov.api.v1.requests.users.auth.RefreshTokenRequest
import ru.kheynov.api.v1.requests.users.auth.RegisterViaEmailRequest
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.users.auth.LoginViaEmailUseCase
import ru.kheynov.domain.use_cases.users.auth.RefreshTokenUseCase
import ru.kheynov.domain.use_cases.users.auth.RegisterViaEmailUseCase

fun Route.configureUserRoutes(
    useCases: UseCases,
) {
    configureAuthRoutes(useCases)
    route("/user") {
        authenticate {
            get("/rooms") {}
        }

        authenticate {
            get {}
        }
        authenticate {
            delete {

            }
        }

        authenticate {
            patch {}
        }
    }
}

private fun Route.configureAuthRoutes(useCases: UseCases) {
    route("/auth") {
        post("/email/register") { //Register user
            val clientId = call.request.headers["Client-Id"] ?: run {
                call.respond(HttpStatusCode.BadRequest, "No client id provided")
                return@post
            }

            val registerRequest = call.receiveNullable<RegisterViaEmailRequest>()?.let {
                UserDTO.UserEmailRegister(
                    username = it.username, password = it.password, email = it.email, clientId = clientId
                )
            } ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }


            when (val res = useCases.registerViaEmailUseCase(registerRequest)) {
                RegisterViaEmailUseCase.Result.Failed -> {
                    call.respond(HttpStatusCode.InternalServerError)
                    return@post
                }

                is RegisterViaEmailUseCase.Result.Successful -> {
                    call.respond(HttpStatusCode.OK, res.tokenPair)
                    return@post
                }

                RegisterViaEmailUseCase.Result.UserAlreadyExists -> {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                    return@post
                }
            }
        }

        post("/email/login") {
            val clientId = call.request.headers["Client-Id"] ?: run {
                call.respond(HttpStatusCode.BadRequest, "No client id provided")
                return@post
            }
            val (email, password) = call.receiveNullable<LoginViaEmailRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            when (val res = useCases.loginViaEmailUseCase(
                email = email, password = password, clientId = clientId
            )) {
                LoginViaEmailUseCase.Result.Failed -> {
                    call.respond(HttpStatusCode.InternalServerError)
                    return@post
                }

                is LoginViaEmailUseCase.Result.Success -> {
                    call.respond(HttpStatusCode.OK, res.tokenPair)
                    return@post
                }

                LoginViaEmailUseCase.Result.UserNotFound -> {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                    return@post
                }
            }
        }

        authenticate {
            post("/refresh") {
                val clientId = call.request.headers["Client-Id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, "No client id provided")
                    return@post
                }

                val oldRefreshToken = call.receiveNullable<RefreshTokenRequest>()?.oldRefreshToken ?: run {
                    call.respond(HttpStatusCode.BadRequest, "No refresh token provided")
                    return@post
                }

                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: run {
                    call.respond(HttpStatusCode.BadRequest, "No access token provided")
                    return@post
                }

                when (val res = useCases.refreshTokenUseCase(
                    userId = userId, oldRefreshToken = oldRefreshToken, clientId = clientId
                )) {
                    is RefreshTokenUseCase.Result.Success -> {
                        call.respond(HttpStatusCode.OK, res.tokenPair)
                        return@post
                    }

                    RefreshTokenUseCase.Result.NoRefreshTokenFound -> {
                        call.respond(HttpStatusCode.BadRequest, "Invalid refresh token")
                        return@post
                    }

                    RefreshTokenUseCase.Result.RefreshTokenExpired -> {
                        call.respond(HttpStatusCode.BadRequest, "Refresh token expired")
                        return@post
                    }

                    RefreshTokenUseCase.Result.Forbidden -> {
                        call.respond(HttpStatusCode.Forbidden, "Forbidden")
                        return@post
                    }

                    RefreshTokenUseCase.Result.Failed -> {
                        call.respond(HttpStatusCode.InternalServerError)
                        return@post
                    }
                }
            }
        }
    }
}