package org.example.controller

import org.example.service.AuthService
import org.example.service.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService,
) {

    @RequestMapping("/register")
    fun register(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val user = authService.register(request.username, request.password)

        return ResponseEntity.ok("Registered user: ${user.username}")
    }

    @RequestMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val success = authService.login(request.username, request.password)

        return if (success) {
            val token = jwtService.generateToken(request.username)
            ResponseEntity.ok(mapOf("token" to token))
        } else {
            ResponseEntity.status(401).body("Invalid credentials")
        }
    }
}

data class AuthRequest(val username: String, val password: String)
