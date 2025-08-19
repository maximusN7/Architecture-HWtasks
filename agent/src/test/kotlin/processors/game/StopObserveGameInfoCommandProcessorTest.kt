package processors.game

import io.mockk.mockk
import io.mockk.verify
import org.example.core.utils.GameStateListener
import org.example.core.utils.InGameState
import org.example.processors.game.StopObserveGameInfoCommandProcessor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StopObserveGameInfoCommandProcessorTest {

    @Test
    fun `WHEN interpret logout logout EXPECT get same state`() {
        // Arrange
        val initialState: InGameState = mockk(relaxed = true)
        val gameStateListener: GameStateListener = mockk(relaxed = true)
        val processor = StopObserveGameInfoCommandProcessor(gameStateListener)

        // Act
        val newState = processor.process(initialState, listOf())

        // Assert
        assertEquals(initialState, newState)
        verify(exactly = 1) { gameStateListener.stopObserve() }
    }
}
