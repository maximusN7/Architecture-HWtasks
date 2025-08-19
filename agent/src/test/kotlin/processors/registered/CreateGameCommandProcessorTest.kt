package processors.registered

import io.mockk.every
import io.mockk.mockk
import org.example.core.utils.InGameState
import org.example.core.utils.NetworkClient
import org.example.model.MiddlewareResponse
import org.example.processors.registered.CreateGameCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class CreateGameCommandProcessorTest {

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
        val mockedState: InGameState = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = CreateGameCommandProcessor(networkClient)
        val mockedResponseBody: String = """
            {
                "gameId": 1,
                "gameName": "gameName",
                "participantsNames": ["player1", "player2"]
            }
            """.trimIndent()

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns
                MiddlewareResponse(
                    true,
                    mockedResponseBody
                )

        // Act
        val newState = processor.process(mockedState, listOf("create", "gameName", "player1 player2"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("Game gameName was created. To join the game use command: join <your password> 1"))
    }

    @Test
    fun `WHEN process empty data EXPECT get same state`() {
        // Arrange
        val mockedState: InGameState = mockk(relaxed = true)
        val networkClient: NetworkClient = mockk(relaxed = true)
        val processor = CreateGameCommandProcessor(networkClient)

        // Act
        val newState = processor.process(mockedState, listOf("create"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("Use command: create <gameName> <participants names separated with space>"))
    }
}
