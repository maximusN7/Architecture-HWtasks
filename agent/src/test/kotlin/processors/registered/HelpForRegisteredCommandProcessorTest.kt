package processors.registered

import io.mockk.mockk
import org.example.core.utils.InMenuStateRegistered
import org.example.processors.registered.HelpForRegisteredCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class HelpForRegisteredCommandProcessorTest {

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
        val mockedState: InMenuStateRegistered = mockk(relaxed = true)
        val processor = HelpForRegisteredCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(
            output.contains(
                """
            You are currently signed in.
            - To create game type: create <gameName> <participants names separated with space>
            - To join game type: join <your password> <gameId>
            - To logout type: logout
            - To exit type: exit
        """.trimIndent()
            )
        )
    }
}
