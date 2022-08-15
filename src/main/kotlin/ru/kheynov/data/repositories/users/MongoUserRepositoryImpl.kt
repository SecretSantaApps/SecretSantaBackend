package ru.kheynov.data.repositories.users

import org.bson.types.ObjectId
import org.litote.kmongo.SetTo
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
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

    override suspend fun updateUsernameByID(id: String, username: String): Boolean {
        val bsonId = ObjectId(id)
        if (users.findOne(User::id eq bsonId) == null) return false
        if (getUserByUsername(username) != null) return false
        return users.updateOne(User::id eq bsonId, setValue(User::username, username)).wasAcknowledged()
    }

    override suspend fun updatePasswordByID(id: String, password: String, salt: String): Boolean {
        val bsonId = ObjectId(id)
        if (users.findOne(User::id eq bsonId) == null) return false
        return users.updateMany(
            User::id eq bsonId, SetTo(User::password, password), SetTo(User::salt, salt)
        ).wasAcknowledged()
    }

    override suspend fun updateUserByID(id: String, user: User): Boolean {
        val bsonId = ObjectId(id)
        if (users.findOne(User::id eq bsonId) == null) return false
        if (getUserByUsername(user.username) != null) return false
        return users.updateMany(
            User::id eq bsonId,
            SetTo(User::username, user.username),
            SetTo(User::password, user.password),
            SetTo(User::salt, user.salt)
        ).wasAcknowledged()
    }
}