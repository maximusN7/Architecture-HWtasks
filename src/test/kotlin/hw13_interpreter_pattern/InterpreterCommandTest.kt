package org.example.hw13_interpreter_pattern

import hw7_vertical_scaling_and_synchronization.ServerThread
import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.example.hw10_microservice_architecture.UsersAccessValidator
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.command.MoveCommand
import org.example.hw3_abstractions.command.RotateCommand
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.GameScheduler
import org.example.hw8_message_broadcast_systems.model.Message
import org.example.hw8_message_broadcast_systems.setup.InitEndpointIoC
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.awt.Point
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class InterpreterCommandTest {

    companion object {

        @JvmStatic
        @BeforeAll
        fun setupIoC() {
            IoC.clear()
            InitEndpointIoC.invoke()
            GameScheduler.clearGames()
        }
    }

    @Test
    fun WHEN_gameId_and_objectId_are_null_no_token_and_id_is_3_EXPECT_see_hardstop_command_in_queue() {
        // Arrange
        val message = Message(
            gameId = null,
            gameObjectId = null,
            operationId = 3L,
            args = mapOf(),
        )

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        InterpreterCommand(
            message = message,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()

        // Assert
        assertEquals(1, serverThread.commandQueue.size)
        assertIs<HardStopCommand>(serverThread.commandQueue.peek())
    }

    @Test
    fun WHEN_gameId_and_objectId_are_null_no_token_and_id_is_2_EXPECT_exception_trying_to_get_rotate_command() {
        // Arrange
        val message = Message(
            gameId = null,
            gameObjectId = null,
            operationId = 2L,
            args = mapOf(),
        )

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        val exception = kotlin.runCatching {
            InterpreterCommand(
                message = message,
                baseScope = baseScope,
                gameThread = gameThread,
                serverThread = serverThread,
            ).invoke()
        }.exceptionOrNull()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        assertNotNull(exception)
    }

    @Test
    fun WHEN_gameId_and_objectId_are_set_no_token_and_id_is_1_EXPECT_exception_trying_to_get_username() {
        // Arrange
        val message = Message(
            gameId = 1L,
            gameObjectId = 3L,
            operationId = 1L,
            args = mapOf("velocity" to "1"),
        )

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        val exception = kotlin.runCatching {
            InterpreterCommand(
                message = message,
                baseScope = baseScope,
                gameThread = gameThread,
                serverThread = serverThread,
            ).invoke()
        }.exceptionOrNull()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        assertNotNull(exception)
    }

    @Test
    fun WHEN_gameId_and_objectId_are_set_token_and_id_is_1_EXPECT_movecommand_in_game_queue() {
        // Arrange
        val message = Message(
            gameId = 3L,
            gameObjectId = 3L,
            operationId = 1L,
            args = mapOf("velocity" to "1"),
        )

        mockkObject(UsersAccessValidator)

        every { UsersAccessValidator.getUsername(message) } returns "agent1"

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        InterpreterCommand(
            message = message,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        val game = GameScheduler.getGameById(3L)
        assertEquals(1, game?.gameQueue?.size)
        assertIs<MoveCommand>(game?.gameQueue?.peek())

        unmockkObject(UsersAccessValidator)
    }

    @Test
    fun WHEN_gameId_and_objectId_are_set_token_and_id_is_5_EXPECT_shootcommand_in_game_queue() {
        // Arrange
        val message = Message(
            gameId = 4L,
            gameObjectId = 3L,
            operationId = 5L,
            args = mapOf("velocity" to "1"),
        )

        mockkObject(UsersAccessValidator)

        every { UsersAccessValidator.getUsername(message) } returns "agent1"

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        InterpreterCommand(
            message = message,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        val game = GameScheduler.getGameById(4L)
        assertEquals(1, game?.gameQueue?.size)
        assertIs<ShootCommand>(game?.gameQueue?.peek())

        unmockkObject(UsersAccessValidator)
    }

    @Test
    fun WHEN_gameId_and_objectId_are_set_token_and_id_is_2_EXPECT_rotatecommand_in_game_queue() {
        // Arrange
        val message = Message(
            gameId = 1L,
            gameObjectId = 3L,
            operationId = 2L,
            args = mapOf(),
        )

        mockkObject(UsersAccessValidator)

        every { UsersAccessValidator.getUsername(message) } returns "agent1"

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        InterpreterCommand(
            message = message,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        val game = GameScheduler.getGameById(1L)
        assertEquals(1, game?.gameQueue?.size)
        assertIs<RotateCommand>(game?.gameQueue?.peek())

        unmockkObject(UsersAccessValidator)
    }

    @Test
    fun WHEN_gameId_and_objectId_are_set_token_and_id_is_2_for_different_users_EXPECT_movecommand_in_game_queue_and_not_affect_different_users_objects() {
        // Arrange
        val message1 = Message(
            gameId = 2L,
            gameObjectId = 3L,
            operationId = 1L,
            args = mapOf("velocity" to "8"),
        )
        val message2 = Message(
            gameId = 2L,
            gameObjectId = 3L,
            operationId = 1L,
            args = mapOf("velocity" to "-8"),
        )

        mockkObject(UsersAccessValidator)

        every { UsersAccessValidator.getUsername(message1) } returns "agent1"
        every { UsersAccessValidator.getUsername(message2) } returns "agent2"

        val baseScope = IoC.resolve("Scopes.Current") as IScope
        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
        val serverThread = ServerThread(commandQueue)
        val gameThread = ServerThread(commandQueue)

        // Act
        InterpreterCommand(
            message = message1,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()
        InterpreterCommand(
            message = message2,
            baseScope = baseScope,
            gameThread = gameThread,
            serverThread = serverThread,
        ).invoke()

        // Assert
        assertEquals(0, serverThread.commandQueue.size)
        val game = GameScheduler.getGameById(2L)
        println("AAAAAQ@@@@ ${game?.gameQueue}")
        assertEquals(2, game?.gameQueue?.size)
        assertIs<MoveCommand>(game?.gameQueue?.peek())

        game?.gameQueue?.poll()?.invoke()
        game?.gameQueue?.poll()?.invoke()
        assertEquals(0, game?.gameQueue?.size)

        val obj1 = game?.getObjectById(3L)
        val obj2 = game?.getObject2ById(3L)

        assertEquals(Point(5, 8), obj1?.getProperty(Property.LOCATION))
        assertEquals(Point(19, 2), obj2?.getProperty(Property.LOCATION))

        unmockkObject(UsersAccessValidator)
    }
}
