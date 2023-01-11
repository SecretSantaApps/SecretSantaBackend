package ru.kheynov.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.api.v1.requests.users.auth.RegisterViaEmailRequest
import ru.kheynov.domain.entities.UserDTO
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.domain.use_cases.users.auth.RegisterViaEmailUseCase

fun Route.configureUserRoutes(
    useCases: UseCases,
) {
    configureAuthRoutes(useCases)
    route("/user") {
        authenticate {
            get("/rooms") {
            }
        }

        authenticate {
            get {
            }
        }
        authenticate {
            delete {

            }
        }

        authenticate {
            patch {
            }
        }
    }
}

private fun Route.configureAuthRoutes(useCases: UseCases) {
    post("/email/register") { //Register user
        val registerRequest = call.receiveNullable<RegisterViaEmailRequest>()?.let {
            UserDTO.UserEmailRegister(
                username = it.username,
                password = it.password,
                email = it.email
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

    // TODO: login, refresh
}