package hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.command.PrintMessageCommand
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class PrintMessageCommandTest {

    @Test
    fun WHEN_invoke_EXPECT_print_message() {
        // Arrange
        val message = "test message"

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        PrintMessageCommand(message).invoke()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertEquals("incoming message: test message", actualOutput)
    }
}
