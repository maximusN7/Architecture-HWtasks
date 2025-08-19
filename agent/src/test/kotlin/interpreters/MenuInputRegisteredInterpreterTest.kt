package interpreters

import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.example.contract.IClientState
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.InMenuStateRegistered
import org.example.core.utils.InMenuStateUnregistered
import org.example.interpreters.MenuInputRegisteredInterpreter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class MenuInputRegisteredInterpreterTest {

    @BeforeEach
    fun setupTest() {
        mockkObject(ExceptionHandler)
    }

    @AfterEach
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `WHEN interpret command create EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk()
        val interpreter = MenuInputRegisteredInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("create")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret logout logout EXPECT get same state`() {
        // Arrange
        val initialState: InMenuStateRegistered = mockk(relaxed = true)
        val interpreter = MenuInputRegisteredInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("logout")

        // Assert
        assertIs<InMenuStateUnregistered>(newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command help EXPECT get same state`() {
        // Arrange
        val initialState: InMenuStateRegistered = mockk(relaxed = true)
        val interpreter = MenuInputRegisteredInterpreter(initialState)

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
        val interpreter = MenuInputRegisteredInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("wrong command")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command exit EXPECT get state in menu registered`() {
        // Arrange
        val initialState: InMenuStateRegistered = mockk(relaxed = true)
        val interpreter = MenuInputRegisteredInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("exit")

        // Assert
        assertNull(newState)
        verify(exactly = 0) { ExceptionHandler.handle(any(), any()).process(any(), any()) }
    }

    @Test
    fun `WHEN interpret command throws error EXPECT get same state`() {
        // Arrange
        val initialState: IClientState = mockk(relaxed = true)
        val interpreter = MenuInputRegisteredInterpreter(initialState)

        // Act
        val newState = interpreter.interpret("join")

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 2) { ExceptionHandler.handle(any(), any()) }
    }
}
