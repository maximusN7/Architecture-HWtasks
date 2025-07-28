package org.example.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.example.security.loadPrivateKey
import org.springframework.stereotype.Service
import java.security.PrivateKey
import java.util.*

@Service
class JwtService {

    private val privateKey: PrivateKey = loadPrivateKey()

    fun generateToken(gameId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setSubject(gameId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()
    }

    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (e: Exception) {
            null
        }
    }

    fun isTokenValid(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username
    }
}

private const val EXPIRATION_TIME = 1000 * 60 * 60 // 1 hour
