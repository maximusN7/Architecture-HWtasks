package org.example.controller

import org.example.service.JwtService
import org.example.service.AuthService
import org.example.service.GameService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(
    private val gameService: GameService,
    private val authService: AuthService,
    private val jwtService: JwtService,
) {

    @RequestMapping("/register")
    fun registerGame(@RequestBody request: GameRequest): ResponseEntity<Any> {
        val game = gameService.registerGame(request.gameName, request.participantsNames)

        return if (game != null) {
            ResponseEntity.ok(
                """
                {
                    "gameId": "${game.gameId}",
                    "gameName": "${request.gameName}",
                    "participantsNames": ${game.participantsNames.convertToJsonStyle()}
                }
            """.trimIndent()
            )
        } else {
            ResponseEntity.status(401).body("All participants are not existing")
        }
    }

    @RequestMapping("/join")
    fun joinGame(@RequestBody request: GameJoinRequest): ResponseEntity<Any> {
        val success = authService.login(request.username, request.password)

        return if (success) {
            if (gameService.isUserParticipantOfGame(request.username, request.gameId)) {
                val token = jwtService.generateToken(request.username, request.gameId)
                ResponseEntity.ok(mapOf("token" to token))
            } else {
                ResponseEntity.status(401).body("This game is not for you")
            }
        } else {
            ResponseEntity.status(401).body("Invalid credentials or unknown game Id")
        }
    }

    private fun List<String>.convertToJsonStyle(): String {
        val namesString = StringBuilder().append("[")
        this.forEachIndexed { index, name ->
            if (index != this.lastIndex) {
                namesString.append("\"$name\", ")
            } else {
                namesString.append("\"$name\"")
            }
        }
        namesString.append("]")
        return namesString.toString()
    }
}

data class GameRequest(val gameName: String, val participantsNames: List<String>)
data class GameJoinRequest(val username: String, val password: String, val gameId: Long)
