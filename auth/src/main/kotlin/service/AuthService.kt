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

    /**
     * Check if user exists, then login else register
     */
    fun signIn(username: String, password: String): Pair<User, Boolean?> {
        val hashed = passwordEncoder.encode(password)

        val existingUser = userRepository.findByUsername(username)
        return if (existingUser != null) {
            if (login(username, password)) {
                Pair(existingUser, false)
            } else {
                Pair(existingUser, null)
            }
        } else {
            val newUser = User(username = username, password = hashed)
            userRepository.save(newUser)
            Pair(newUser, true)
        }
    }

    fun login(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username) ?: return false

        return passwordEncoder.matches(password, user.password)
    }
}
