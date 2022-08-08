package ru.kheynov.data.rooms

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import ru.kheynov.domain.entities.Room
import ru.kheynov.domain.repositories.RoomsRepository

class MongoRoomsRepositoryImpl(
    db: CoroutineDatabase,
) : RoomsRepository {
    private val rooms = db.getCollection<Room>("rooms")

    override suspend fun createRoom(room: Room): Boolean {
        if (rooms.findOne(Room::name eq room.name) != null) return false
        return rooms.insertOne(room).wasAcknowledged()
    }

    override suspend fun deleteRoomByName(name: String): Boolean {
        if (rooms.findOne(Room::name eq name) == null) return false
        return rooms.deleteOne(Room::name eq name).wasAcknowledged()
    }

    override suspend fun addUserToRoom(vararg userIds: String, roomName: String): Boolean {
        if (rooms.findOne(Room::name eq roomName) == null) return false
        val usersList = rooms.findOne(Room::name eq roomName)?.usersId?.toMutableList() ?: return false
        usersList.addAll(userIds)
        return rooms.updateOne(Room::name eq roomName, setValue(Room::usersId, usersList)).wasAcknowledged()
    }

    override suspend fun getRoomByName(name: String): Room? {
        deleteRoomByName("dfs")
        return rooms.findOne(Room::name eq name)
    }

    override suspend fun deleteUserFromRoom(userId: String, roomName: String): Boolean {
        if (rooms.findOne(Room::name eq roomName) == null) return false
        val usersList = rooms.findOne(Room::name eq roomName)?.usersId?.toMutableList() ?: return false
        var isSuccessful = usersList.remove(userId)
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::name eq roomName, setValue(Room::usersId, usersList)).wasAcknowledged()
        }
        return isSuccessful
    }
}