package ru.kheynov.security.jwt.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService : TokenService {
    override fun generateTokenPair(config: TokenConfig, vararg claims: TokenClaim): TokenPair {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.accessLifetime))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        val accessToken = token.sign(Algorithm.HMAC256(config.secret))
        val refreshToken = UUID.randomUUID().toString()
        val refreshTokenExpiration = System.currentTimeMillis() + config.refreshLifetime
        return TokenPair(accessToken, RefreshToken(refreshToken, refreshTokenExpiration))
    }
}
