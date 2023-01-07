package ru.kheynov.data.mappers

import ru.kheynov.data.entities.User
import ru.kheynov.domain.entities.UserDTO

fun toDataUser(user: UserDTO.User): User = User {
    userId = user.userId
    name = user.username
    email = user.email
    passwordHash = user.passwordHash
    refreshToken = user.refreshToken
    refreshTokenExpiration = user.refreshTokenExpiration
    authProvider = user.authProvider
}

fun User.mapToUser(): UserDTO.User = UserDTO.User(
    userId = this.userId,
    username = this.name,
    email = this.email,
    passwordHash = this.passwordHash,
    refreshToken = this.refreshToken,
    refreshTokenExpiration = this.refreshTokenExpiration,
    authProvider = this.authProvider
)
