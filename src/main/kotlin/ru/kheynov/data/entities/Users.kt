package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.text

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    var userId: String
    var name: String
}

fun User.mapToUser(): ru.kheynov.domain.entities.User {
    return ru.kheynov.domain.entities.User(this.userId, this.name)
}

object Users : Table<User>("users") {
    var userId = text("user_id").bindTo { it.userId }
    var name = text("name").bindTo { it.name }
}