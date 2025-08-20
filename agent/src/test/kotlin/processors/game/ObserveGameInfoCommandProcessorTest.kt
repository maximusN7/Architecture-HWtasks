package processors.game

import io.mockk.mockk
import io.mockk.verify
import org.example.core.utils.GameStateListener
import org.example.core.utils.InGameState
import org.example.processors.game.ObserveGameInfoCommandProcessor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ObserveGameInfoCommandProcessorTest {

    @Test
    fun `WHEN interpret logout logout EXPECT get same state`() {
        // Arrange
        val initialState: InGameState = mockk(relaxed = true)
        val gameStateListener: GameStateListener = mockk(relaxed = true)
        val processor = ObserveGameInfoCommandProcessor(gameStateListener)

        // Act
        val newState = processor.process(initialState, listOf())

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 1) { gameStateListener.startObserve(any()) }
    }
}
