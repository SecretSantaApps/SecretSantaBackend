package ru.kheynov.utils

import java.util.*

fun getRandomUsername(): String = UUID.randomUUID().toString().subSequence(0..6).toString()
fun getRandomPassword(): String = UUID.randomUUID().toString().subSequence(0..6).toString()
fun getRandomRoomID(): String = UUID.randomUUID().toString().subSequence(0..6).toString()
fun getRandomUserID(): String = UUID.randomUUID().toString().subSequence(0..7).toString()