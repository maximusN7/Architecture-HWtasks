package org.example.controller

import org.example.service.JwtService
import org.example.service.AuthService
import org.example.service.GameService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GameController(
    private val gameService: GameService,
    private val authService: AuthService,
    private val jwtService: JwtService,
) {

    @RequestMapping("/register")
    fun registerGame(@RequestBody request: GameRequest): String {
        val gameId = gameService.registerGame(request.gameName, request.participantsNames)

        return "Game ${request.gameName} created. Game Id: $gameId"
    }

    @RequestMapping("/join")
    fun login(@RequestBody request: GameJoinRequest): ResponseEntity<Any> {
        val success = authService.login(request.username, request.password)

        return if (success) {
            if (gameService.isUserParticipantOfGame(request.username, request.gameId)) {
                val token = jwtService.generateToken(request.gameId)
                ResponseEntity.ok(mapOf("token" to token))
            } else {
                ResponseEntity.status(401).body("This game is not for you")
            }
        } else {
            ResponseEntity.status(401).body("Invalid credentials or unknown game Id")
        }
    }
}

data class GameRequest(val gameName: String, val participantsNames: List<String>)
data class GameJoinRequest(val username: String, val password: String, val gameId: Long)
