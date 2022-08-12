package ru.kheynov.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.kheynov.di.ServiceLocator
import ru.kheynov.security.token.TokenConfig

fun Application.configureSecurity(
    config: TokenConfig = ServiceLocator.tokenConfig,
) {

    authentication {
        jwt {
            realm = System.getenv("JWT_REALM")
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }

}
