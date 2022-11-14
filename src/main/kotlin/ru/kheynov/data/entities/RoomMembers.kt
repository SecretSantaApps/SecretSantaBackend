package ru.kheynov.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.text

interface RoomMember : Entity<RoomMember> {
    companion object : Entity.Factory<RoomMember>()

    var roomName: Room
    var userId: User
    var recipient: User?
}

object RoomMembers : Table<RoomMember>("room_members") {
    var roomName = text("room_name").references(Rooms) { it.roomName }
    var userId = text("user_id").references(Users) { it.userId }
    var recipient = text("recipient").references(Users) { it.recipient }
}