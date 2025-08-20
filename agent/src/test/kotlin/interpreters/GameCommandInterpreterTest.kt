package interpreters

import org.example.interpreters.GameCommandInterpreter
import org.example.interpreters.GamePlayCommand
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameCommandInterpreterTest {

    @Test
    fun `WHEN interpret command move EXPECT get id 1L`() {
        // Arrange
        val interpreter = GameCommandInterpreter()

        // Act
        val commandId = interpreter.interpret(GamePlayCommand.MOVE)

        // Assert
        assertEquals(1L, commandId)
    }

    @Test
    fun `WHEN interpret rotate move EXPECT get id 2L`() {
        // Arrange
        val interpreter = GameCommandInterpreter()

        // Act
        val commandId = interpreter.interpret(GamePlayCommand.ROTATE)

        // Assert
        assertEquals(2L, commandId)
    }

    @Test
    fun `WHEN interpret shoot move EXPECT get id 3L`() {
        // Arrange
        val interpreter = GameCommandInterpreter()

        // Act
        val commandId = interpreter.interpret(GamePlayCommand.SHOOT)

        // Assert
        assertEquals(3L, commandId)
    }
}
