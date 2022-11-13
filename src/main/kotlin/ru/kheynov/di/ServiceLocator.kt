package ru.kheynov.di

import org.ktorm.database.Database
import ru.kheynov.data.repositories.users.PostgresUsersRepository

object ServiceLocator {
    val db = Database.connect(
        url = System.getenv("DATABASE_CONNECTION_STRING"),
        driver = "org.postgresql.Driver",
        user = System.getenv("POSTGRES_NAME"),
        password = System.getenv("POSTGRES_PASSWORD")
    )
    val usersRepository = PostgresUsersRepository(db)

}