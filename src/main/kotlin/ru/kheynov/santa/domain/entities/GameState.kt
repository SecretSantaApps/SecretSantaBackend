package ru.kheynov.domain.entities

class GameState {
    companion object {
        const val WAITING_FOR_PLAYERS = "waiting_for_players"
        const val WAITING_FOR_ACCEPTANCE = "waiting_for_acceptance"
        const val GAME_STARTED = "game_started"
    }
}