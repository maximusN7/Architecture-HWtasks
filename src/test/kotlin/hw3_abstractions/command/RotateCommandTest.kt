package hw3_abstractions.command

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.adapter.RotatingObjectAdapter
import org.example.hw3_abstractions.command.RotateCommand
import org.example.hw3_abstractions.model.Angle
import org.junit.jupiter.api.Test
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
}
