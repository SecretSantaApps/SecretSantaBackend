package ru.kheynov.data.repositories.rooms

import org.litote.kmongo.contains
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
        val usersList = rooms.findOne(Room::name eq roomName)?.userIds?.toMutableList() ?: return false
        usersList.addAll(userIds)
        return rooms.updateOne(Room::name eq roomName, setValue(Room::userIds, usersList)).wasAcknowledged()
    }

    override suspend fun getRoomByName(name: String): Room? {
        return rooms.findOne(Room::name eq name)
    }

    override suspend fun deleteUserFromRoom(userId: String, roomName: String): Boolean {
        if (rooms.findOne(Room::name eq roomName) == null) return false
        val usersList = rooms.findOne(Room::name eq roomName)?.userIds?.toMutableList() ?: return false
        var isSuccessful = usersList.remove(userId)
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::name eq roomName, setValue(Room::userIds, usersList)).wasAcknowledged()
        }
        return isSuccessful
    }

    override suspend fun generateRelations(roomName: String): Map<String, String> {
        if (rooms.findOne(Room::name eq roomName) == null) return emptyMap()
        val usersList = rooms.findOne(Room::name eq roomName)?.userIds?.toMutableList() ?: return emptyMap()
        val relationsMap = mutableMapOf<String, String>()
        usersList.shuffle()
        for (i in usersList.indices) {
            if (i == 0) continue
            relationsMap[usersList[i - 1]] = usersList[i]
        }
        relationsMap[usersList.last()] = usersList.first()
        if (!rooms.updateOne(Room::name eq roomName, setValue(Room::relations, relationsMap)).wasAcknowledged()) {
            return emptyMap()
        }
        return relationsMap
    }

    override suspend fun clearRelations(roomName: String): Boolean {
        if (rooms.findOne(Room::name eq roomName) == null) return false
        return rooms.updateOne(Room::name eq roomName, setValue(Room::relations, null)).wasAcknowledged()
    }

    override suspend fun getUserRooms(userId: String): List<Room> {
        return rooms.find(Room::userIds.contains(userId)).toList()
    }
}