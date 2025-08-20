package processors.unregistered

import io.mockk.every
import io.mockk.mockk
import org.example.core.utils.InMenuStateRegistered
import org.example.core.utils.InMenuStateUnregistered
import org.example.core.utils.NetworkClient
import org.example.model.MiddlewareResponse
import org.example.processors.unregistered.LoginCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginCommandProcessorTest {

    private val outputStream = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        // Заменяем System.out на наш outputStream для перехвата println
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun restoreStreams() {
        // Восстанавливаем оригинальный поток вывода
        System.setOut(originalOut)
    }

    @Test
    fun `WHEN process normal data EXPECT get result`() {
        // Arrange
        val mockedState: InMenuStateUnregistered = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = LoginCommandProcessor(networkClient)
        val mockedResponseBody: String = """
            {
                "username": "player",
                "isNewUser": true
            }
            """.trimIndent()

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns
                MiddlewareResponse(true, mockedResponseBody)

        // Act
        val newState = processor.process(mockedState, listOf("login", "player", "password"))

        // Assert
        val output = outputStream.toString()
        assertIs<InMenuStateRegistered>(newState)
        assert(output.contains("Welcome, player! To create a game use command: create <gameName> <participants names separated with space>"))
    }

    @Test
    fun `WHEN process wrong password data EXPECT get result`() {
        // Arrange
        val mockedState: InMenuStateUnregistered = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = LoginCommandProcessor(networkClient)
        val mockedResponseBody: String = """
            {
                "username": "player",
                "isNewUser": null
            }
            """.trimIndent()

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns
                MiddlewareResponse(true, mockedResponseBody)

        // Act
        val newState = processor.process(mockedState, listOf("login", "player", "password"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("It's wrong password for player!"))
    }

    @Test
    fun `WHEN process correct data old user EXPECT get result`() {
        // Arrange
        val mockedState: InMenuStateUnregistered = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = LoginCommandProcessor(networkClient)
        val mockedResponseBody: String = """
            {
                "username": "player",
                "isNewUser": false
            }
            """.trimIndent()

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns
                MiddlewareResponse(true, mockedResponseBody)

        // Act
        val newState = processor.process(mockedState, listOf("login", "player", "password"))

        // Assert
        val output = outputStream.toString()
        assertIs<InMenuStateRegistered>(newState)
        assert(output.contains("Welcome back, player!"))
    }

    @Test
    fun `WHEN process empty data EXPECT get same state`() {
        // Arrange
        val mockedState: InMenuStateUnregistered = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = LoginCommandProcessor(networkClient)

        // Act
        val newState = processor.process(mockedState, listOf("login"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("Use command: login <user> <password>"))
    }
}
