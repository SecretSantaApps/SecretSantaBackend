package ru.kheynov.di

import org.ktorm.database.Database
import ru.kheynov.data.repositories.rooms.PostgresRoomsRepository
import ru.kheynov.data.repositories.users.PostgresUsersRepository
import ru.kheynov.domain.use_cases.UseCases
import ru.kheynov.utils.GiftDispenser
import ru.kheynov.utils.SimpleCycleGiftDispenser

object ServiceLocator {
    private val db = Database.connect(
        url = System.getenv("DATABASE_CONNECTION_STRING"),
        driver = "org.postgresql.Driver",
        user = System.getenv("POSTGRES_NAME"),
        password = System.getenv("POSTGRES_PASSWORD")
    )

    private val usersRepository = PostgresUsersRepository(db)
    private val roomsRepository = PostgresRoomsRepository(db)

    private val giftDispenser: GiftDispenser = SimpleCycleGiftDispenser()

    val useCases = UseCases(roomsRepository, usersRepository)
}