package ru.kheynov.domain.services

import ru.kheynov.domain.entities.Notification


interface OneSignalService {
    suspend fun sendNotification(notification: Notification): Boolean

    companion object {
        const val APP_ID = "e281e33f-9662-49c7-ad62-71bad853fc3a"
        const val BASE_URL = "https://onesignal.com/api/v1/notifications"
        val API_KEY: String = System.getenv("ONESIGNAL_API_KEY")
    }
}