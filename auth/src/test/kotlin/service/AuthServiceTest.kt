package service

import io.mockk.every
import io.mockk.mockk
import org.example.model.User
import org.example.repository.UserRepository
import org.example.service.AuthService
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

class AuthServiceTest {

    @Test
    fun `WHEN signIn EXPECT get pair`() {
        // Arrange
        val userRepository: UserRepository = mockk()
        val passwordEncoder: PasswordEncoder = mockk()
        val user = User(
            id = 0L,
            username = "player",
            password = "password"
        )
        every { passwordEncoder.encode("password") } returns "hased"
        every { userRepository.findByUsername("player") } returns user
        every { passwordEncoder.matches("password", user.password) } returns true

        val service = AuthService(userRepository, passwordEncoder)

        // Act
        val signInResult = service.signIn("player", "password")

        // Assert
        assertEquals(user, signInResult.first)
        assertEquals(false, signInResult.second)
    }
}
