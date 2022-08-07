package ru.kheynov.data.user

import org.bson.types.ObjectId
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

    override suspend fun deleteUserByID(id: String): Boolean {
        val bsonId = ObjectId(id)
        if (users.findOne(User::id eq bsonId) == null) return false
        return users.deleteOne(User::id eq bsonId).wasAcknowledged()
    }

    override suspend fun getUserByID(id: String): User? {
        val bsonId = ObjectId(id)
        return users.findOne(User::id eq bsonId)
    }

    override suspend fun editUserByID(id: String, user: User): Boolean {
        val bsonId = ObjectId(id)
        if (users.findOne(User::id eq bsonId) == null) return false
        users.deleteOne(User::id eq bsonId)
        return users.insertOne(user).wasAcknowledged()
    }
}