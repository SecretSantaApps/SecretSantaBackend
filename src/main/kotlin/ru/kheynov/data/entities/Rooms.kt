package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.text
import java.time.LocalDate

interface Room : Entity<Room> {
    companion object : Entity.Factory<Room>()

    val id: Int
    var name: String
    var password: String?
    var date: LocalDate?
    var ownerId: String
    var maxPrice: Int?
}

fun Room.mapToRoom(): ru.kheynov.domain.entities.Room {
    return ru.kheynov.domain.entities.Room(
        this.name, this.password, this.date, this.ownerId, this.maxPrice
    )
}

object Rooms : Table<Room>("rooms") {
    val id = int("id").primaryKey().bindTo(Room::id)
    var name = text("name").bindTo(Room::name)
    var password = text("password").bindTo(Room::password)
    var date = date("date").bindTo(Room::date)
    var ownerId = text("owner_id").bindTo(Room::ownerId)
    var maxPrice = int("max_price").bindTo(Room::maxPrice)
}