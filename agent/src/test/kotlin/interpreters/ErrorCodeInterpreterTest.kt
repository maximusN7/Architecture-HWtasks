package interpreters

import org.example.exceptions.AuthErrorException
import org.example.interpreters.ErrorCodeInterpreter
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class ErrorCodeInterpreterTest {

    @Test
    fun `WHEN interpret code 403 EXPECT get AuthErrorException instance`() {
        // Arrange
        val interpreter = ErrorCodeInterpreter()

        // Act
        val error = interpreter.interpret(403)

        // Assert
        assertIs<AuthErrorException>(error)
    }

    @Test
    fun `WHEN interpret something else EXPECT get Exception instance`() {
        // Arrange
        val interpreter = ErrorCodeInterpreter()

        // Act
        val error = interpreter.interpret(0)

        // Assert
        assertIs<Exception>(error)
    }
}
