package command

import org.example.adapter.MovingObjectAdapter
import org.example.command.MoveCommand
import org.example.model.Angle
import org.example.model.Property
import org.example.model.SpaceShip
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MoveCommandTest {

    @Test
    fun WHEN_velocity_is_8_and_initial_location_is_12_5_EXPECT_new_location_be_5_8() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        MoveCommand(movingObjectAdapter).invoke()

        // Assert
        assertEquals(Point(5, 8), movingObjectAdapter.getLocation())
    }

    @Test
    fun WHEN_velocity_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_angle_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
        }
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_location_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)

        // Act
        val result = runCatching {
            MoveCommand(movingObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
}
