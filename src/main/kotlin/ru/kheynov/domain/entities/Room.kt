package ru.kheynov.domain.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Room(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val password: String?,
    val creatorId: String,
    val usersId: List<String>,
    val relations: Map<String, String>? = null,
)
