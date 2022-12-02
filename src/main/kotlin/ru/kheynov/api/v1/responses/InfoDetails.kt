package ru.kheynov.api.v1.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kheynov.domain.entities.UserInfo
import ru.kheynov.utils.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class InfoDetails(
    @SerialName("room_name") val roomName: String,
    @SerialName("owner_id") val ownerId: String,
    val date: @Serializable(with = LocalDateSerializer::class) LocalDate?,
    @SerialName("max_price") val maxPrice: Int?,
    val users: List<UserInfo>,
    val recipient: String? = null,
)