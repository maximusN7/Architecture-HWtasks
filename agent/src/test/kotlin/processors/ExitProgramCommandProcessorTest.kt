package processors

import io.mockk.mockk
import org.example.contract.IClientState
import org.example.processors.ExitProgramCommandProcessor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertNull

class ExitProgramCommandProcessorTest {

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
        val mockedState: IClientState = mockk(relaxed = true)
        val processor = ExitProgramCommandProcessor()

        // Act
        val newState = processor.process(mockedState, listOf())

        // Assert
        val output = outputStream.toString()
        assertNull(newState)
        assert(output.contains("Exit..."))
    }
}
