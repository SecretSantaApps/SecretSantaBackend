package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.text

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    var userId: String
    var name: String
}

object Users : Table<User>("users") {
    var userId = text("user_id").primaryKey().bindTo(User::userId)
    var name = text("name").bindTo(User::name)
}