package ru.kheynov

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.mozilla.javascript.Token
import org.slf4j.event.Level
import ru.kheynov.api.v1.routing.v1Routes
import ru.kheynov.di.appModule
import ru.kheynov.security.jwt.token.TokenConfig

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureDI()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureSecurity()
}

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
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
        openAPI(path = "/openapi", swaggerFile = "openapi/documentation.yaml")
        swaggerUI(path = "/swagger", swaggerFile = "openapi/documentation.yaml")
    }
}

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}

fun Application.configureSecurity() {
    val config: TokenConfig by inject()
    authentication {
        jwt {
            realm = System.getenv("jwt-realm")
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { token ->
                if (
                    token.payload.audience.contains(config.audience) &&
                    token.payload.expiresAt.time > System.currentTimeMillis()
                ) {
                    JWTPrincipal(token.payload)
                } else null
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = false
            explicitNulls = false
        })
    }
}