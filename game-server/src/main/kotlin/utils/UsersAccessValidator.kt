package org.example.utils

import io.jsonwebtoken.Jwts
import org.example.model.Message

private val publicKey = loadPublicKey()

fun validateUsersAccess(message: Message): Boolean {
    val gameId = message.gameId
    val token = message.args["token"]

    if (token != null) {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .body
            val tokenGameId = claims.subject

            return gameId == tokenGameId?.toLong()
        } catch (e: Exception) {
            return false
        }
    }

    return true
}

object UsersAccessValidator {

    fun getUsername(message: Message): String? {
        val token = message.args["token"]

        if (token != null) {
            try {
                val claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .body
                val tokenUsername = claims["username"] as String?

                return tokenUsername
            } catch (e: Exception) {
                return null
            }
        }

        return null
    }
}
