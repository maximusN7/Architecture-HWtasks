package processors.game

import io.mockk.every
import io.mockk.mockk
import org.example.core.utils.InGameState
import org.example.processors.game.HelpInGameCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class HelpInGameCommandProcessorTest {

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
        every { mockedState.currentGameId } returns 1
        val processor = HelpInGameCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assertEquals(mockedState, newState)
        assert(
            output.contains(
                """
            You are currently in game 1
            - To move type: move <velocity>
            - To rotate type: rotate <angular velocity>
            - To shoot type: shoot
            - To exit in menu type: exit
        """.trimIndent()
            )
        )
    }
}
