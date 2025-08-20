package processors.unregistered

import io.mockk.mockk
import org.example.core.utils.InMenuStateUnregistered
import org.example.processors.unregistered.GameFromUnregisteredCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class GameFromUnregisteredCommandProcessorTest {

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
        val mockedState: InMenuStateUnregistered = mockk()
        val processor = GameFromUnregisteredCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(output.contains("You are unregistered. Login before play"))
    }
}
