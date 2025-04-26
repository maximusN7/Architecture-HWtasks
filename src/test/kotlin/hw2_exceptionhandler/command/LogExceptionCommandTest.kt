package hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.command.LogExceptionCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class LogExceptionCommandTest {

    @Test
    fun WHEN_invoke_EXPECT_print_information_about_command_and_exception() {
        // Arrange
        val command: ICommand = mock()
        val exception: Exception = mock()

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        LogExceptionCommand(command, exception).invoke()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertTrue(actualOutput.contains("during proceeding of ICommand"))
        assertTrue(actualOutput.contains("occurred exception Mock for Exception"))
    }
}
