package ru.kheynov

import io.ktor.server.application.*
import ru.kheynov.plugins.configureHTTP
import ru.kheynov.plugins.configureMonitoring
import ru.kheynov.plugins.configureSecurity
import ru.kheynov.plugins.configureSerialization
import ru.kheynov.routing.configureRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
