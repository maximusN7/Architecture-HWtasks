package processors.registered

import io.mockk.every
import io.mockk.mockk
import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.core.utils.NetworkClient
import org.example.model.MiddlewareResponse
import org.example.model.UserData
import org.example.processors.registered.JoinGameCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertIs

class JoinGameCommandProcessorTest {

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
        val mockedState: InMenuStateRegistered = mockk(relaxed = true)
        every { mockedState.currentUser } returns UserData("userName", false)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = JoinGameCommandProcessor(networkClient)
        val mockedResponseBody: String = """
            {
                "token": "token"
            }
            """.trimIndent()

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns
                MiddlewareResponse(
                    true,
                    mockedResponseBody
                )

        // Act
        val newState = processor.process(mockedState, listOf("join", "password", "1"))

        // Assert
        val output = outputStream.toString()
        assertIs<InGameState>(newState)
        assert(output.contains("Congrats! You have join the game 1"))
    }

    @Test
    fun `WHEN process empty data EXPECT get same state`() {
        // Arrange
        val mockedState: InMenuStateRegistered = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = JoinGameCommandProcessor(networkClient)

        // Act
        val newState = processor.process(mockedState, listOf("join"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("Use command: join <your password> <gameId>"))
    }
}
