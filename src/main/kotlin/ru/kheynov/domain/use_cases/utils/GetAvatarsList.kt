package ru.kheynov.domain.use_cases.utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kheynov.domain.entities.AvatarDTO
import ru.kheynov.domain.repositories.UsersRepository

class GetAvatarsList : KoinComponent {
    private val usersRepository: UsersRepository by inject()
    suspend operator fun invoke(): List<AvatarDTO> =
        usersRepository.getAvailableAvatars()
}