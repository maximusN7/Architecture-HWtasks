package hw6_adapter_and_bridge

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.command.MoveCommand
import org.example.hw3_abstractions.command.RotateCommand
import org.example.hw3_abstractions.contract.IMovingObject
import org.example.hw3_abstractions.contract.IRotatingObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.ioc.command.InitScopeBasedIoCCommand
import org.example.hw5_ioc.utils.ILambda
import org.example.hw6_adapter_and_bridge.ioc.generating.CreateInterfaceAdapterStrategy
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.AdapterFactoryNotExistingException
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class ProcessorTest {

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
        }
    }

    // region MovingObject
    @Test
    fun WHEN_try_get_moving_object_adapter_and_move_EXPECT_get_correct_location() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }

        // Act
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IMovingObject>(movingObjectAdapter)

        movingObjectAdapter.setLocation(Point(12, 5))
        movingObjectAdapter.finish()
        MoveCommand(movingObjectAdapter).invoke()
        assertEquals(Point(5, 8), movingObjectAdapter.getLocation())
    }

    @Test
    fun WHEN_try_get_moving_object_adapter_and_get_velocity_EXPECT_get_correct_value() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }

        // Act
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IMovingObject>(movingObjectAdapter)

        movingObjectAdapter.setLocation(Point(12, 5))
        movingObjectAdapter.finish()
        MoveCommand(movingObjectAdapter).invoke()
        assertEquals(Pair(-7, 3), movingObjectAdapter.getVelocity())
    }

    @Test
    fun WHEN_try_get_moving_object_adapter_and_get_not_setted_velocity_EXPECT_get_exception() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.ANGLE, Angle(3, 7))
        }

        // Act
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IMovingObject>(movingObjectAdapter)

        movingObjectAdapter.setLocation(Point(12, 5))
        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_try_get_moving_object_adapter_and_get_not_setted_angle_EXPECT_get_exception() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
        }

        // Act
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IMovingObject>(movingObjectAdapter)

        movingObjectAdapter.setLocation(Point(12, 5))
        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_try_get_moving_object_adapter_and_get_not_setted_location_EXPECT_get_exception() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }

        // Act
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IMovingObject>(movingObjectAdapter)

        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
    // endregion

    // region RotatingObject
    @Test
    fun WHEN_try_get_rotating_object_adapter_and_rotate_EXPECT_get_correct_angle() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
            setProperty(Property.ANGLE, Angle(3, 8))
        }

        // Act
        val rotatingObjectAdapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))

        // Assert
        assertIs<IRotatingObject>(rotatingObjectAdapter)

        RotateCommand(rotatingObjectAdapter).invoke()
        assertEquals(Angle(5, 8), rotatingObjectAdapter.getAngle())
    }

    @Test
    fun WHEN_try_get_rotating_object_adapter_and_rotate_EXPECT_get_correct_angle_and_velocity() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
            setProperty(Property.ANGLE, Angle(2, 8))
            setProperty(Property.VELOCITY, 8)
        }

        // Act
        val rotatingObjectAdapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))
        val movingObjectAdapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

        // Assert
        assertIs<IRotatingObject>(rotatingObjectAdapter)
        assertIs<IMovingObject>(movingObjectAdapter)

        movingObjectAdapter.setLocation(Point(0, 0))

        val initialVelocity = movingObjectAdapter.getVelocity()

        RotateCommand(rotatingObjectAdapter).invoke()

        assertEquals(Angle(4, 8), rotatingObjectAdapter.getAngle())
        val newVelocity = movingObjectAdapter.getVelocity()
        assertEquals(Pair(0, 8), initialVelocity)
        assertEquals(Pair(-8, 0), newVelocity)
        assertNotEquals(initialVelocity, newVelocity)
    }

    @Test
    fun WHEN_try_get_rotating_object_adapter_angle_is_not_set_EXPECT_get_exception() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
        }

        // Act
        val rotatingObjectAdapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))

        // Assert
        assertIs<IRotatingObject>(rotatingObjectAdapter)

        val result = runCatching {
            RotateCommand(rotatingObjectAdapter).invoke()
        }.exceptionOrNull()

        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_try_get_rotating_object_adapter_angular_velocity_is_not_set_EXPECT_get_exception() {
        // Arrange
        val uObject: UObject = SpaceShip().apply {
            setProperty(Property.ANGLE, Angle(2, 8))
        }

        // Act
        val rotatingObjectAdapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))

        // Assert
        assertIs<IRotatingObject>(rotatingObjectAdapter)

        val result = runCatching {
            RotateCommand(rotatingObjectAdapter).invoke()
        }.exceptionOrNull()

        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
    // endregion
}
