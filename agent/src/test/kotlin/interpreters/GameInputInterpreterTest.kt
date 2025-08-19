package interpreters

import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.example.contract.IClientState
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.interpreters.GameInputInterpreter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GameInputInterpreterTest {

    @BeforeEach
    fun setupTest() {
        mockkObject(ExceptionHandler)
    }

    @AfterEach
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `WHEN interpret command move EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk()
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("move")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command rotate EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk()
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("rotate")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command help EXPECT get same state`() {
        // Arrange
        val initialState: InGameState = mockk(relaxed = true)
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("help")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command random EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk()
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("wrong command")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command exit EXPECT get state in menu registered`() {
        // Arrange
        val initialState: InGameState = mockk(relaxed = true)
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("exit")

        // Assert
        assertIs<InMenuStateRegistered>(newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command throws error EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk(relaxed = true)
        val interpreter = GameInputInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("exit")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 1) { ExceptionHandler.handle(any(), any()) }
    }
}
