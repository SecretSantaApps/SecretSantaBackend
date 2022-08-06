package ru.kheynov.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import ru.kheynov.domain.entities.User
import ru.kheynov.domain.repositories.UserRepository

class MongoUserRepositoryImpl(
    db: CoroutineDatabase,
) : UserRepository {

    private val users = db.getCollection<User>("users")
    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

}