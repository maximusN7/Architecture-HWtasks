package hw2_exceptionhandler

import org.example.hw2_exceptionhandler.EventLooper
import org.example.hw2_exceptionhandler.ExceptionHandler
import org.example.hw2_exceptionhandler.command.PrintMessageCommand
import org.example.hw2_exceptionhandler.command.RepeatOnceCommand
import org.example.hw2_exceptionhandler.command.RepeatTwiceCommand
import org.example.hw2_exceptionhandler.command.ThrowExceptionCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw2_exceptionhandler.handler.PutInQueueLoggerHandler
import org.example.hw2_exceptionhandler.handler.PutInQueueRepeatOnceHandler
import org.example.hw2_exceptionhandler.handler.PutInQueueRepeatTwiceElseLogHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.LinkedList
import kotlin.test.BeforeTest

class EventLooperTest {

    @BeforeTest
    fun setup() {
        // HW point 5
        ExceptionHandler.register(
            ThrowExceptionCommand::class.java,
            IllegalArgumentException::class.java,
            PutInQueueLoggerHandler::handle
        )
        // HW point 7
        ExceptionHandler.register(
            ThrowExceptionCommand::class.java,
            NullPointerException::class.java,
            PutInQueueRepeatOnceHandler::handle
        )
        // HW point 8
        ExceptionHandler.register(
            ThrowExceptionCommand::class.java,
            NumberFormatException::class.java,
            PutInQueueRepeatOnceHandler::handle
        )
        ExceptionHandler.register(
            RepeatOnceCommand::class.java,
            NumberFormatException::class.java,
            PutInQueueLoggerHandler::handle
        )
        // HW point 9
        ExceptionHandler.register(
            ThrowExceptionCommand::class.java,
            ArithmeticException::class.java,
            PutInQueueRepeatTwiceElseLogHandler::handle
        )
        ExceptionHandler.register(
            RepeatTwiceCommand::class.java,
            Exception::class.java,
            PutInQueueRepeatOnceHandler::handle
        )
        ExceptionHandler.register(
            RepeatOnceCommand::class.java,
            ArithmeticException::class.java,
            PutInQueueLoggerHandler::handle
        )
    }

    // HW point 5 test
    @Test
    fun WHEN_in_queue_one_command_throws_exception_that_require_log_EXPECT_print_log_about_exception() {
        // Arrange
        val commands = LinkedList<ICommand>().apply {
            add(PrintMessageCommand("test 1"))
            add(PrintMessageCommand("test 2"))
            add(ThrowExceptionCommand(IllegalArgumentException("test exception")))
            add(PrintMessageCommand("test 3"))
        }
        EventLooper.addCommands(commands)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        EventLooper.processCommands()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertEquals(expectedOutputForLogException, actualOutput)
    }

    // HW point 7 test
    @Test
    fun WHEN_in_queue_one_command_throws_exception_that_require_repeat_command_EXPECT_repeat_command() {
        // Arrange
        val commands = LinkedList<ICommand>().apply {
            add(PrintMessageCommand("test 1"))
            add(PrintMessageCommand("test 2"))
            add(ThrowExceptionCommand(NullPointerException("test exception")))
            add(PrintMessageCommand("test 3"))
        }
        EventLooper.addCommands(commands)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        EventLooper.processCommands()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertEquals(expectedOutputForRepeatCommandOnce, actualOutput)
    }

    // HW point 8 test
    @Test
    fun WHEN_in_queue_one_command_throws_exception_that_require_repeat_and_log_command_EXPECT_repeat_and_log_command() {
        // Arrange
        val commands = LinkedList<ICommand>().apply {
            add(PrintMessageCommand("test 1"))
            add(PrintMessageCommand("test 2"))
            add(ThrowExceptionCommand(NumberFormatException("test exception")))
            add(PrintMessageCommand("test 3"))
        }
        EventLooper.addCommands(commands)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        EventLooper.processCommands()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertEquals(expectedOutputForRepeatCommandOnceAndAfterLog, actualOutput)
    }

    // HW point 9 test
    @Test
    fun WHEN_in_queue_one_command_throws_exception_that_require_repeat_twice_command_EXPECT_repeat_and_log_command() {
        // Arrange
        val commands = LinkedList<ICommand>().apply {
            add(PrintMessageCommand("test 1"))
            add(PrintMessageCommand("test 2"))
            add(ThrowExceptionCommand(ArithmeticException("test exception")))
            add(PrintMessageCommand("test 3"))
        }
        EventLooper.addCommands(commands)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Act
        EventLooper.processCommands()

        // Assert
        val actualOutput = outputStream.toString().trim()
        assertEquals(expectedOutputForRepeatCommandTwiceAndAfterLog, actualOutput)
    }
}
