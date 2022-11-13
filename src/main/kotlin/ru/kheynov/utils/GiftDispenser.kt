package ru.kheynov.utils

interface GiftDispenser {
    fun getRandomDistribution(users: List<String>): List<Map<String, String>>
}

class SimpleCycleGiftDispenser() : GiftDispenser {
    override fun getRandomDistribution(users: List<String>): List<Map<String, String>> {
        val result: MutableList<Map<String, String>> = mutableListOf()

        users.forEachIndexed { index, userId ->
            if (index == users.size - 1) {
                result.add(mapOf(userId to users[0]))
            } else {
                result.add(mapOf(userId to users[index + 1]))
            }
        }
        return result.toList()
    }
}
