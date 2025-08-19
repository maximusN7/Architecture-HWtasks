package command

import org.example.adapter.MovingObjectAdapter
import org.example.adapter.RotatingObjectAdapter
import org.example.command.RotateCommand
import org.example.model.Angle
import org.example.model.Property
import org.example.model.SpaceShip
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class RotateCommandTest {

    @Test
    fun WHEN_angular_velocity_is_90_and_initial_angle_is_135_degrees_EXPECT_new_angle_be_225() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
            setProperty(Property.ANGLE, Angle(3, 8))
        }
        val rotatingObjectAdapter = RotatingObjectAdapter(spaceShip)

        // Act
        RotateCommand(rotatingObjectAdapter).invoke()

        // Assert
        assertEquals(Angle(5, 8), rotatingObjectAdapter.getAngle())
    }

    @Test
    fun WHEN_angular_velocity_is_90_and_initial_angle_is_90_degrees_EXPECT_new_angle_be_180_and_change_of_velocity() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
            setProperty(Property.ANGLE, Angle(2, 8))
            setProperty(Property.VELOCITY, 8)
        }
        val rotatingObjectAdapter = RotatingObjectAdapter(spaceShip)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(0, 0))

        val initialVelocity = movingObjectAdapter.getVelocity()

        // Act
        RotateCommand(rotatingObjectAdapter).invoke()

        // Assert
        assertEquals(Angle(4, 8), rotatingObjectAdapter.getAngle())
        val newVelocity = movingObjectAdapter.getVelocity()
        assertEquals(Pair(0, 8), initialVelocity)
        assertEquals(Pair(-8, 0), newVelocity)
        assertNotEquals(initialVelocity, newVelocity)
    }

    @Test
    fun WHEN_angle_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.ANGULAR_VELOCITY, 2.toByte())
        }
        val rotatingObjectAdapter = RotatingObjectAdapter(spaceShip)

        // Act
        val result = runCatching {
            RotateCommand(rotatingObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_angular_velocity_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.ANGLE, Angle(2, 8))
        }
        val rotatingObjectAdapter = RotatingObjectAdapter(spaceShip)

        // Act
        val result = runCatching {
            RotateCommand(rotatingObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
}
