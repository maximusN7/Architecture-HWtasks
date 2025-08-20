package org.example.controller

import com.google.gson.Gson
import org.example.clients.HttpClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(
    private val httpClient: HttpClient,
) {

    @RequestMapping("/register")
    fun registerGame(@RequestBody request: GameRequest): ResponseEntity<Any> {
        val response = httpClient.sendJsonToGameService(Gson().toJson(request), CREATE_GAME_PATH)

        return if (response.isSuccess) {
            ResponseEntity.ok(response.body)
        } else {
            ResponseEntity.status(401).body("AuthError")
        }
    }

    @RequestMapping("/join")
    fun joinGame(@RequestBody request: GameJoinRequest): ResponseEntity<Any> {
        val response = httpClient.sendJsonToGameService(Gson().toJson(request), JOIN_GAME_PATH)

        return if (response.isSuccess) {
            ResponseEntity.ok(response.body)
        } else {
            ResponseEntity.status(401).body("AuthError")
        }
    }
}

data class GameRequest(val gameName: String, val participantsNames: List<String>)
data class GameJoinRequest(val username: String, val password: String, val gameId: Long)

private const val CREATE_GAME_PATH = "/register"
private const val JOIN_GAME_PATH = "/join"
