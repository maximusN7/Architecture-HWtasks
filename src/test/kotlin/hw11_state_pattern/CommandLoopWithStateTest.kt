package hw11_state_pattern

import hw7_vertical_scaling_and_synchronization.ServerThread
import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import hw7_vertical_scaling_and_synchronization.commands.SynchronizationCommand
import org.example.hw11_state_pattern.CommandLoopWithState
import org.example.hw11_state_pattern.DefaultState
import org.example.hw11_state_pattern.MoveToState
import org.example.hw11_state_pattern.commands.MoveToCommand
import org.example.hw11_state_pattern.commands.RunCommand
import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.command.MoveCommand
import org.example.hw3_abstractions.contract.IMovingObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.ioc.command.InitScopeBasedIoCCommand
import org.example.hw5_ioc.utils.ILambda
import org.example.hw6_adapter_and_bridge.ioc.generating.CreateInterfaceAdapterStrategy
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.AdapterFactoryNotExistingException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.awt.Point
import java.util.concurrent.CountDownLatch
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class CommandLoopWithStateTest {

    companion object {

        @JvmStatic
        @BeforeAll
        fun setupIoC() {
            IoC.clear()
            InitScopeBasedIoCCommand(
                defaultMissingStrategy = { _, _ -> throw AdapterFactoryNotExistingException() }
            ).invoke()

            IoC.resolve(
                "IoC.Register",
                listOf(
                    "Adapter",
                    ILambda { _, params ->
                        CreateInterfaceAdapterStrategy(
                            params?.get(0) as KClass<out Any>,
                            params[1] as UObject
                        ).resolve()
                    }
                )
            )
        }
    }


    @Test
    fun WHEN_queue_includes_moveing_commands_and_synchronize_points_EXCEPT_get_correct_position_on_checkpoints() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 1)
            setProperty(Property.ANGLE, Angle(0, 4))
        }
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject)) as IMovingObject
        movingObjectAdapter.setLocation(Point(0, 0))

        val commandLoop = CommandLoopWithState()

        val latch = CountDownLatch(1)
        val serverThreadOne = ServerThread(commandLoop.commandQueueOne, actionAfter = { latch.countDown() })

        commandLoop.servers.add(serverThreadOne)

        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }
        val latch11 = CountDownLatch(1)
        val latch12 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch11, latch12))
        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }
        val latch21 = CountDownLatch(1)
        val latch22 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch21, latch22))
        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }

        val latch31 = CountDownLatch(1)
        val latch32 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch31, latch32))
        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }

        val latch41 = CountDownLatch(1)
        val latch42 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch41, latch42))
        for (i in 0..9) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }
        commandLoop.commandQueueOne.add(HardStopCommand(serverThreadOne))

        // Act
        commandLoop.startObserving()

        // Assert
        latch11.await()
        assertEquals(Point(20, 0), movingObjectAdapter.getLocation())
        latch12.countDown()


        latch21.await()
        assertEquals(Point(40, 0), movingObjectAdapter.getLocation())
        latch22.countDown()

        latch31.await()
        assertEquals(Point(60, 0), movingObjectAdapter.getLocation())
        latch32.countDown()

        latch41.await()
        assertEquals(Point(80, 0), movingObjectAdapter.getLocation())
        latch42.countDown()

        latch.await()

        assertEquals(Point(90, 0), movingObjectAdapter.getLocation())
    }

    @Test
    fun WHEN_queue_includes_moving_commands_and_hard_stop_command_in_the_middle_EXCEPT_get_half_movement_to_end() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 1)
            setProperty(Property.ANGLE, Angle(0, 4))
        }
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject)) as IMovingObject
        movingObjectAdapter.setLocation(Point(0, 0))

        val commandLoop = CommandLoopWithState()

        val latch = CountDownLatch(1)
        val serverThreadOne = ServerThread(commandLoop.commandQueueOne, actionAfter = { latch.countDown() })

        commandLoop.servers.add(serverThreadOne)

        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }
        commandLoop.commandQueueOne.add(HardStopCommand(serverThreadOne))
        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }

        // Act
        commandLoop.startObserving()

        // Assert
        latch.await()

        assertEquals(Point(20, 0), movingObjectAdapter.getLocation())
    }

    @Test
    fun WHEN_queue_includes_moving_commands_and_move_to_command_in_the_middle_EXCEPT_half_commands_on_other_server_and_return_to_default_state_after_runCommand() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 1)
            setProperty(Property.ANGLE, Angle(0, 4))
        }
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject)) as IMovingObject
        movingObjectAdapter.setLocation(Point(0, 0))

        val commandLoop = CommandLoopWithState()

        val latch = CountDownLatch(1)
        val serverThreadOne = ServerThread(commandLoop.commandQueueOne, actionAfter = { latch.countDown() })
        val serverThreadTwo = ServerThread(commandLoop.commandQueueTwo)

        commandLoop.servers.add(serverThreadOne)
        commandLoop.servers.add(serverThreadTwo)

        val latch01 = CountDownLatch(1)
        val latch02 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch01, latch02))

        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }
        commandLoop.commandQueueOne.add(MoveToCommand())

        val latch11 = CountDownLatch(1)
        val latch12 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch11, latch12))

        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }

        val latch21 = CountDownLatch(1)
        val latch22 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch21, latch22))

        commandLoop.commandQueueOne.add(RunCommand())

        val latch31 = CountDownLatch(1)
        val latch32 = CountDownLatch(1)
        commandLoop.commandQueueOne.add(SynchronizationCommand(latch31, latch32))

        for (i in 0..19) {
            commandLoop.commandQueueOne.add(MoveCommand(movingObjectAdapter))
        }

        commandLoop.commandQueueOne.add(HardStopCommand(serverThreadOne))

        // Act
        commandLoop.startObserving()

        // Assert
        latch01.await()
        assertEquals(Point(0, 0), movingObjectAdapter.getLocation())
        assertIs<DefaultState>(commandLoop.states[0])
        assertIs<DefaultState>(commandLoop.states[1])
        latch02.countDown()

        latch11.await()
        assertEquals(Point(20, 0), movingObjectAdapter.getLocation())
        assertIs<MoveToState>(commandLoop.states[0])
        assertIs<DefaultState>(commandLoop.states[1])
        latch12.countDown()

        latch21.await()
        assertIs<DefaultState>(commandLoop.states[0])
        assertIs<DefaultState>(commandLoop.states[1])
        latch22.countDown()

        latch31.await()
        assertEquals(Point(40, 0), movingObjectAdapter.getLocation())
        assertIs<DefaultState>(commandLoop.states[0])
        assertIs<DefaultState>(commandLoop.states[1])
        latch32.countDown()

        latch.await()

        assertNull(commandLoop.states[0])
        assertIs<DefaultState>(commandLoop.states[1])
        assertEquals(Point(60, 0), movingObjectAdapter.getLocation())
    }
}
