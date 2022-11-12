package ru.kheynov

import io.ktor.server.application.*
import ru.kheynov.plugins.*
import ru.kheynov.security.firebase.auth.FirebaseAdmin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    FirebaseAdmin.init()
    configureHTTP()
    configureFirebaseAuth()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
