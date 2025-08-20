package processors.game

import io.mockk.every
import io.mockk.mockk
import org.example.core.utils.InGameState
import org.example.core.utils.NetworkClient
import org.example.interpreters.GameCommandInterpreter
import org.example.model.MiddlewareResponse
import org.example.processors.game.ShootCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class ShootCommandProcessorTest {

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
        val gameCommandInterpreter: GameCommandInterpreter = mockk(relaxed = true)
        val processor = ShootCommandProcessor(networkClient, gameCommandInterpreter)

        every { networkClient.sendJsonToMiddleware(any(), any()) } returns MiddlewareResponse(true)

        // Act
        val newState = processor.process(mockedState, listOf("shoot"))

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("Spaceship has shot"))
    }
}
