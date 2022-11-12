package ru.kheynov.security.firebase.auth

import com.google.firebase.FirebaseApp

object FirebaseAdmin {
    fun init(): FirebaseApp = FirebaseApp.initializeApp()
}