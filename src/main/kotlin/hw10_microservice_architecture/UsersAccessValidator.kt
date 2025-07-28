package org.example.hw10_microservice_architecture

import io.jsonwebtoken.Jwts
import org.example.hw8_message_broadcast_systems.model.Message

private val publicKey = loadPublicKey()

fun validateUsersAccess(message: Message): Boolean {
    val gameId = message.gameId
    val token = message.args["token"]

    if (token != null) {
        try {
            val tokenGameId = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .body
                .subject

            return gameId == tokenGameId.toLong()
        } catch (e: Exception) {
            return false
        }
    }

    return true
}
