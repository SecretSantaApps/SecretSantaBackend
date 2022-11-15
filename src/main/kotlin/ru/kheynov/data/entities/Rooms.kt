package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDate

interface Room : Entity<Room> {
    companion object : Entity.Factory<Room>()

    var name: String
    var password: String?
    var date: LocalDate?
    var ownerId: String
    var maxPrice: Int?
    var gameStarted: Boolean
}


object Rooms : Table<Room>("rooms") {
    var name = text("name").primaryKey().bindTo(Room::name)
    var password = text("password").bindTo(Room::password)
    var date = date("date").bindTo(Room::date)
    var ownerId = text("owner_id").bindTo(Room::ownerId)
    var maxPrice = int("max_price").bindTo(Room::maxPrice)
    var gameStarted = boolean("game_started").bindTo { it.gameStarted }
}