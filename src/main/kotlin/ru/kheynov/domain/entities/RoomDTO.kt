package ru.kheynov.domain.entities

import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kheynov.utils.LocalDateSerializer
import java.time.LocalDate

sealed interface RoomDTO {

    @Serializable
    data class Room(
        @SerialName("room_name") val name: String,
        val password: String,
        @SerialName("room_id") val id: String,
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate?,
        @SerialName("owner_id") val ownerId: String,
        @SerialName("max_price") val maxPrice: Int? = null,
        @SerialName("game_started") val gameStarted: Boolean = false,
        @SerialName("members_count") val membersCount: Int,
    ) : RoomDTO

    @Serializable
    data class RoomUpdate(
        val password: String? = null,
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate? = null,
        @SerialName("max_price") val maxPrice: Int? = null,
    ) : RoomDTO

    @Serializable
    data class RoomInfo(
        @SerialName("room_name") val name: String,
        @SerialName("room_id") val id: String,
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate?,
        @SerialName("owner_id") val ownerId: String,
        @SerialName("max_price") val maxPrice: Int? = null,
        @SerialName("game_started") val gameStarted: Boolean = false,
        @SerialName("members_count") val membersCount: Int,
    ) : RoomDTO
}
