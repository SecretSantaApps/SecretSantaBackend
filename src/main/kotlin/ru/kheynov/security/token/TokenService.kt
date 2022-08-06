package ru.kheynov.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim,
    ): String
}