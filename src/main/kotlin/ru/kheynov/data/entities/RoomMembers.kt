package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface RoomMember : Entity<RoomMember> {
    companion object : Entity.Factory<RoomMember>()

    val roomId: Room
    val userId: User
    val recipient: User
}

object RoomMembers : Table<RoomMember>("room_members") {
    val roomId = int("room_id").references(Rooms) { it.roomId }
    val userId = int("user_id").references(Users) { it.userId }
    val recipient = int("recipient").references(Users) { it.recipient }
}