package ru.kheynov.api.v1.routing

import io.ktor.server.routing.*


fun Route.configureAuthRoutes(

) {
    route("/user") {

    }
}
//
//fun Route.authenticate(
//    userRepository: UserRepository,
//) {
//    authenticate(FIREBASE_AUTH) {
//        get("/authenticate") {
//            val userId = call.principal<UserAuth>()?.userId.toString()
//            if (userRepository.getUserByID(userId) == null) {
//                call.respond(HttpStatusCode.Unauthorized)
//                return@get
//            }
//            call.respond(HttpStatusCode.OK)
//        }
//    }
//}