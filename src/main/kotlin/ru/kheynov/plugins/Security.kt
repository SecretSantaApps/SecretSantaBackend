package ru.kheynov.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import ru.kheynov.security.firebase.auth.UserAuth
import ru.kheynov.security.firebase.auth.firebase

fun Application.configureFirebaseAuth() {
    install(Authentication) {
        firebase {
            validate {
                UserAuth(it.uid, it.name)
            }
        }
    }
}