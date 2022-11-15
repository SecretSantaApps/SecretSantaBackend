package ru.kheynov.utils

interface GiftDispenser {
    fun getRandomDistribution(users: List<String>): List<Pair<String, String>>
}

class SimpleCycleGiftDispenser : GiftDispenser {
    override fun getRandomDistribution(users: List<String>): List<Pair<String, String>> {
        val result: MutableList<Pair<String, String>> = mutableListOf()

        users.forEachIndexed { index, userId ->
            if (index == users.size - 1) {
                result.add(userId to users[0])
            } else {
                result.add(userId to users[index + 1])
            }
        }
        return result.toList()
    }
}
