package processors.unregistered

import io.mockk.mockk
import org.example.core.utils.InMenuStateUnregistered
import org.example.processors.unregistered.HelpForUnregisteredCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class HelpForUnregisteredCommandProcessorTest {

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
        val mockedState: InMenuStateUnregistered = mockk(relaxed = true)
        val processor = HelpForUnregisteredCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(
            output.contains(
                """
            You are currently not signed in.
            - To sign in/sign up type: login <username> <password>
            - To exit type: exit
        """.trimIndent()
            )
        )
    }
}
