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
    val name: String
    val date: LocalDate
    val maxPrice: Int
}

object Rooms : Table<Room>("rooms") {
    val id = int("id").primaryKey().bindTo(Room::id)
    val name = text("name").bindTo(Room::name)
    val date = date("date").bindTo(Room::date)
    val maxPrice = int("max_price").bindTo(Room::maxPrice)
}