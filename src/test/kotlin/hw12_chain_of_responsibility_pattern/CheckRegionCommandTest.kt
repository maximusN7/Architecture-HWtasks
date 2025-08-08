package hw12_chain_of_responsibility_pattern

import hw7_vertical_scaling_and_synchronization.ServerThread
import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import hw7_vertical_scaling_and_synchronization.commands.StartLooperCommand
import hw7_vertical_scaling_and_synchronization.commands.SynchronizationCommand
import org.example.hw12_chain_of_responsibility_pattern.CheckRegionCommand
import org.example.hw12_chain_of_responsibility_pattern.IGameFieldObject
import org.example.hw12_chain_of_responsibility_pattern.Region
import org.example.hw12_chain_of_responsibility_pattern.RegionSystem
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckRegionCommandTest {

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
    fun WHEN_object_moves_into_another_object_position_EXPECT_collision_state_changed_for_object() {
        val currentObject = getObjectWithLocation(1, 5)
        val regionsAndObjects = mutableMapOf(
            Region(Point(0, 0), Point(10, 10)) to mutableListOf(
                getObjectWithLocation(5, 5).first,
                getObjectWithLocation(5, 7).first,
                currentObject.first,
            ),
            Region(Point(10, 0), Point(20, 10)) to mutableListOf(
                getObjectWithLocation(15, 7).first,
            ),
        )
        val regionSystem = RegionSystem(
            regionSize = 10,
            pointOfMeasureShift = 0,
            regionsAndObjects = regionsAndObjects
        )

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..3) {
            commandQueue.add(MoveCommand(currentObject.second))
            commandQueue.add(CheckRegionCommand(regionSystem, currentObject.first))
        }
        commandQueue.add(HardStopCommand(serverThread))

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch.await()

        assertEquals(Point(5, 5), currentObject.first.getLocation())
        assertEquals(Point(5, 5), currentObject.second.getLocation())
        assertTrue(currentObject.first.getIsCollided())
    }

    @Test
    fun WHEN_object_moves_and_not_intercept_other_object_positions_EXPECT_collision_state_stays_false() {
        val currentObject = getObjectWithLocation(1, 5)
        val regionsAndObjects = mutableMapOf(
            Region(Point(0, 0), Point(10, 10)) to mutableListOf(
                getObjectWithLocation(5, 5).first,
                getObjectWithLocation(5, 7).first,
                currentObject.first,
            ),
            Region(Point(10, 0), Point(20, 10)) to mutableListOf(
                getObjectWithLocation(15, 7).first,
            ),
        )
        val regionSystem = RegionSystem(
            regionSize = 10,
            pointOfMeasureShift = 0,
            regionsAndObjects = regionsAndObjects
        )

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..2) {
            commandQueue.add(MoveCommand(currentObject.second))
            commandQueue.add(CheckRegionCommand(regionSystem, currentObject.first))
        }
        commandQueue.add(HardStopCommand(serverThread))

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch.await()

        assertEquals(Point(4, 5), currentObject.first.getLocation())
        assertEquals(Point(4, 5), currentObject.second.getLocation())
        assertFalse(currentObject.first.getIsCollided())
    }

    @Test
    fun WHEN_object_moves_to_another_region_EXPECT_move_it_from_old_region_list_to_second() {
        val currentObject = getObjectWithLocation(1, 6)
        val regionsAndObjects = mutableMapOf(
            Region(Point(0, 0), Point(10, 10)) to mutableListOf(
                getObjectWithLocation(5, 5).first,
                getObjectWithLocation(5, 7).first,
                currentObject.first,
            ),
            Region(Point(10, 0), Point(20, 10)) to mutableListOf(
                getObjectWithLocation(15, 7).first,
            ),
        )
        val regionSystem = RegionSystem(
            regionSize = 10,
            pointOfMeasureShift = 0,
            regionsAndObjects = regionsAndObjects
        )

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..7) {
            commandQueue.add(MoveCommand(currentObject.second))
            commandQueue.add(CheckRegionCommand(regionSystem, currentObject.first))
        }
        val latch11 = CountDownLatch(1)
        val latch12 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch11, latch12))
        for (i in 0..1) {
            commandQueue.add(MoveCommand(currentObject.second))
            commandQueue.add(CheckRegionCommand(regionSystem, currentObject.first))
        }
        commandQueue.add(HardStopCommand(serverThread))

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch11.await()
        assertEquals(Point(9, 6), currentObject.first.getLocation())
        assertEquals(3, regionSystem.regionsAndObjects[Region(Point(0, 0), Point(10, 10))]?.size)
        assertEquals(1, regionSystem.regionsAndObjects[Region(Point(10, 0), Point(20, 10))]?.size)
        latch12.countDown()

        latch.await()

        assertEquals(Point(11, 6), currentObject.first.getLocation())
        assertEquals(Point(11, 6), currentObject.second.getLocation())
        assertEquals(2, regionSystem.regionsAndObjects[Region(Point(0, 0), Point(10, 10))]?.size)
        assertEquals(2, regionSystem.regionsAndObjects[Region(Point(10, 0), Point(20, 10))]?.size)
        assertFalse(currentObject.first.getIsCollided())
    }

    @Test
    fun WHEN_object_moves_to_border_another_region_EXPECT_check_collision_for_different_regions_and_collide_from_second_check() {
        val currentObject = getObjectWithLocation(1, 6)
        val regionsAndObjectsZeroShift = mutableMapOf(
            Region(Point(0, 0), Point(10, 10)) to mutableListOf(
                getObjectWithLocation(5, 5).first,
                getObjectWithLocation(5, 7).first,
                currentObject.first,
            ),
            Region(Point(10, 0), Point(20, 10)) to mutableListOf(
                getObjectWithLocation(10, 6).first,
            ),
        )

        val regionsAndObjectsThreeShift = mutableMapOf(
            Region(Point(3, 3), Point(13, 13)) to mutableListOf(
                getObjectWithLocation(5, 5).first,
                getObjectWithLocation(5, 7).first,
                getObjectWithLocation(10, 6).first,
                currentObject.first,
            ),
            Region(Point(13, 3), Point(23, 13)) to mutableListOf(),
        )
        val regionSystemZeroShift = RegionSystem(
            regionSize = 10,
            pointOfMeasureShift = 0,
            regionsAndObjects = regionsAndObjectsZeroShift
        )
        val regionSystemThreeShift = RegionSystem(
            regionSize = 10,
            pointOfMeasureShift = 3,
            regionsAndObjects = regionsAndObjectsThreeShift
        )

        val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

        val latch = CountDownLatch(1)
        val serverThread = ServerThread(commandQueue, actionAfter = { latch.countDown() })

        for (i in 0..7) {
            // after every move check with only one system
            commandQueue.add(MoveCommand(currentObject.second))
            commandQueue.add(CheckRegionCommand(regionSystemZeroShift, currentObject.first, isPointObject = false))
        }
        val latch11 = CountDownLatch(1)
        val latch12 = CountDownLatch(1)
        commandQueue.add(SynchronizationCommand(latch11, latch12))

        // check with two systems
        commandQueue.add(CheckRegionCommand(regionSystemZeroShift, currentObject.first, isPointObject = false))
        commandQueue.add(CheckRegionCommand(regionSystemThreeShift, currentObject.first, isPointObject = false))

        commandQueue.add(HardStopCommand(serverThread))

        // Act
        StartLooperCommand(serverThread).invoke()

        // Assert
        latch11.await()
        assertEquals(Point(9, 6), currentObject.first.getLocation())
        assertEquals(3, regionSystemZeroShift.regionsAndObjects[Region(Point(0, 0), Point(10, 10))]?.size)
        assertEquals(1, regionSystemZeroShift.regionsAndObjects[Region(Point(10, 0), Point(20, 10))]?.size)
        assertEquals(4, regionSystemThreeShift.regionsAndObjects[Region(Point(3, 3), Point(13, 13))]?.size)
        assertEquals(0, regionSystemThreeShift.regionsAndObjects[Region(Point(13, 3), Point(23, 13))]?.size)
        // without check from second system there is no collision
        assertFalse(currentObject.first.getIsCollided())
        latch12.countDown()

        latch.await()

        assertEquals(Point(9, 6), currentObject.first.getLocation())
        assertEquals(Point(9, 6), currentObject.second.getLocation())
        assertEquals(3, regionSystemZeroShift.regionsAndObjects[Region(Point(0, 0), Point(10, 10))]?.size)
        assertEquals(1, regionSystemZeroShift.regionsAndObjects[Region(Point(10, 0), Point(20, 10))]?.size)
        assertEquals(4, regionSystemThreeShift.regionsAndObjects[Region(Point(3, 3), Point(13, 13))]?.size)
        assertEquals(0, regionSystemThreeShift.regionsAndObjects[Region(Point(13, 3), Point(23, 13))]?.size)
        // but after check from another system we see, that objects are collided
        assertTrue(currentObject.first.getIsCollided())
    }

    private fun getObjectWithLocation(x: Int, y: Int): Pair<IGameFieldObject, IMovingObject> {
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 1)
            setProperty(Property.ANGLE, Angle(0, 4))
            setProperty(Property.COLLISION_STATE, false)
        }
        val gameFieldObjectAdapter =
            IoC.resolve("Adapter", listOf(IGameFieldObject::class, uObject)) as IGameFieldObject
        gameFieldObjectAdapter.setLocation(Point(x, y))

        val movingObjectAdapter =
            IoC.resolve("Adapter", listOf(IMovingObject::class, uObject)) as IMovingObject

        return Pair(gameFieldObjectAdapter, movingObjectAdapter)
    }
}
