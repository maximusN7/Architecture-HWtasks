package core.utils

import io.mockk.mockk
import org.example.core.utils.ConsoleStringCommand
import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.core.utils.InMenuStateUnregistered
import org.junit.jupiter.api.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

class ConsoleCommandsClientStateTest {

    @Test
    fun `WHEN state is in menu unregistered exit command EXPECT null`() {
        // Arrange
        val state = InMenuStateUnregistered()

        // Act
        val result = state.handle(ConsoleStringCommand("exit"))

        // Assert
        assertNull(result)
    }

    @Test
    fun `WHEN state is in menu registered exit command EXPECT null`() {
        // Arrange
        val state = InMenuStateRegistered(mockk(relaxed = true))

        // Act
        val result = state.handle(ConsoleStringCommand("exit"))

        // Assert
        assertNull(result)
    }

    @Test
    fun `WHEN state is in game exit command EXPECT menu state`() {
        // Arrange
        val state = InGameState(1, mockk(relaxed = true), mockk(relaxed = true))

        // Act
        val result = state.handle(ConsoleStringCommand("exit"))

        // Assert
        assertIs<InMenuStateRegistered>(result)
    }

    @Test
    fun `WHEN state handle null EXPECT get null`() {
        // Arrange
        val state = InMenuStateUnregistered()

        // Act
        val result = state.handle(null)

        // Assert
        assertNull(result)
    }
}
