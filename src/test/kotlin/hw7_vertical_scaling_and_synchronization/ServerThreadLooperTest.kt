package hw7_vertical_scaling_and_synchronization

import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import hw7_vertical_scaling_and_synchronization.commands.SoftStopCommand
import hw7_vertical_scaling_and_synchronization.commands.StartLooperCommand
import hw7_vertical_scaling_and_synchronization.commands.SynchronizationCommand
import org.example.hw2_exceptionhandler.ExceptionHandler
import org.example.hw2_exceptionhandler.contract.ICommand
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
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CountDownLatch
import kotlin.reflect.KClass
import kotlin.test.assertEquals

class ServerThreadLooperTest {

    companion object {

        @JvmStatic
        @BeforeAll
        fun setupIoC() {
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
            IoC.resolve(
                "IoC.Register",
                listOf(
                    "ExceptionHandler",
                    ILambda { _, params ->
                        ExceptionHandler.handle(params?.get(0) as ICommand, params[1] as Exception)
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

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }
        val latch11 = CountDownLatch(1)
        val latch12 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch11, latch12))
        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }
        val latch21 = CountDownLatch(1)
        val latch22 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch21, latch22))
        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }

        val latch31 = CountDownLatch(1)
        val latch32 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch31, latch32))
        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }

        val latch41 = CountDownLatch(1)
        val latch42 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch41, latch42))
        for (i in 0..9) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }
        commandQueue.add(HardStopCommand(serverThread))

        // Act
        StartLooperCommand(serverThread).invoke()

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
    fun WHEN_queue_includes_moving_commands_and_soft_stop_command_in_the_middle_EXCEPT_get_all_movement_to_end() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 1)
            setProperty(Property.ANGLE, Angle(0, 4))
        }
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject)) as IMovingObject
        movingObjectAdapter.setLocation(Point(0, 0))

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }
        commandQueue.add(SoftStopCommand(serverThread))
        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch.await()

        assertEquals(Point(40, 0), movingObjectAdapter.getLocation())
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

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }
        commandQueue.add(HardStopCommand(serverThread))
        for (i in 0..19) {
            commandQueue.add(MoveCommand(movingObjectAdapter))
        }

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch.await()

        assertEquals(Point(20, 0), movingObjectAdapter.getLocation())
    }
}
