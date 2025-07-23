package org.example.service

import org.example.model.User
import org.example.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun register(username: String, password: String): User {
        val hashed = passwordEncoder.encode(password)
        val user = User(username = username, password = hashed)
        return userRepository.save(user)
    }

    fun login(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username) ?: return false

        return passwordEncoder.matches(password, user.password)
    }
}
