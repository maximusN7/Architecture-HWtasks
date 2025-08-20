package service

import io.mockk.*
import org.example.model.MoveRequest
import org.example.service.ApiService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ApiServiceTest {

    @BeforeEach
    fun setup() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns "uuid"
    }

    @AfterEach
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `WHEN map to message EXPECT get message`() {
        // Arrange
        val request = MoveRequest(1, 1, 1, "token", "2")
        val service = ApiService()

        // Act
        val message = service.mapToMessage(request)

        // Assert
        assertEquals("uuid", message.key)
        assertEquals("""{"gameId":1,"gameObjectId":1,"operationId":1,"token":"token","velocity":2}""", message.value)
    }
}
