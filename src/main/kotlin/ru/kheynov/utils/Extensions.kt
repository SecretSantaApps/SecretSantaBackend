package ru.kheynov.utils

import ru.kheynov.domain.repositories.GameRepository
import ru.kheynov.domain.repositories.RoomsRepository
import ru.kheynov.domain.repositories.UsersRepository

typealias GameRepositories = Triple<UsersRepository, RoomsRepository, GameRepository>