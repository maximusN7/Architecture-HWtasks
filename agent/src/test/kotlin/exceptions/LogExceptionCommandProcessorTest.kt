package exceptions

import io.mockk.mockk
import org.example.contract.IClientState
import org.example.exceptions.LogExceptionCommandProcessor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LogExceptionCommandProcessorTest {

    @Test
    fun `WHEN calls exception EXPECT return same state`() {
        // Arrange
        val initialState: IClientState = mockk()
        val processor = LogExceptionCommandProcessor()

        // Act
        val state = processor.process(initialState, listOf())

        // Assert
        assertEquals(initialState, state)
    }
}
