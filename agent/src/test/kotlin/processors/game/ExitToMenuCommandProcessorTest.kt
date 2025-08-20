package processors.game

import io.mockk.mockk
import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.processors.game.ExitToMenuCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertIs

class ExitToMenuCommandProcessorTest {

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
    fun `WHEN process EXPECT see output`() {
        // Arrange
        val mockedState: InGameState = mockk(relaxed = true)
        val processor = ExitToMenuCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assert(output.contains("Exit to menu..."))
        assertIs<InMenuStateRegistered>(newState)
    }
}
