package ru.kheynov.security.firebase.auth

import io.ktor.server.auth.*

data class UserAuth(val userId: String = "", val displayName: String? = "") : Principal