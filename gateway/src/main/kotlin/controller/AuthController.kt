package org.example.controller

import com.google.gson.Gson
import org.example.clients.HttpClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val httpClient: HttpClient,
) {

    @RequestMapping("/signin")
    fun register(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val response = httpClient.sendJsonToAuthService(Gson().toJson(request), SIGN_IN_PATH)

        return if (response.isSuccess) {
            ResponseEntity.ok(response.body)
        } else {
            ResponseEntity.status(response.code ?: 401).body("AuthError")
        }
    }
}

data class AuthRequest(val username: String, val password: String)

private const val SIGN_IN_PATH = "/signin"
