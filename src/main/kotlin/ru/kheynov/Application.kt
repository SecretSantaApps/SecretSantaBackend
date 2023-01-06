package ru.kheynov

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import ru.kheynov.api.v1.routing.v1Routes
import ru.kheynov.domain.entities.UserAuth
import ru.kheynov.security.firebase.auth.FirebaseAdmin
import ru.kheynov.security.firebase.auth.firebase

fun main(args: Array<String>){
    EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    FirebaseAdmin.init()
    configureHTTP()
    configureFirebaseAuth()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("Secret Santa Server")
        }
        route("/api") {
            v1Routes()
        }
    }
}

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    }
}

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}

fun Application.configureFirebaseAuth() {
    install(Authentication) {
        firebase {
            validate {
                UserAuth(it.uid, it.name ?: it.email ?: "")
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = false
                explicitNulls = false
            }
        )
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}