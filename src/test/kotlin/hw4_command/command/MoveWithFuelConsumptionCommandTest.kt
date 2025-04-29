package hw4_command.command

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.adapter.MovingObjectAdapter
import org.example.hw3_abstractions.model.Angle
import org.example.hw4_command.adapter.RefuelableObjectAdapter
import org.example.hw4_command.command.MoveWithFuelConsumptionCommand
import org.example.hw4_command.model.CommandException
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MoveWithFuelConsumptionCommandTest {

    @Test
    fun WHEN_fuel_consumption_is_2_and_initial_fuel_is_10_and_movement_params_set_EXPECT_fuel_become_8_and_new_position() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 2)
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()

        // Assert
        assertEquals(8, refuelableObjectAdapter.getFuel())
        assertEquals(Point(5, 8), movingObjectAdapter.getLocation())
    }

    @Test
    fun WHEN_fuel_consumption_is_more_than_initial_fuel_and_movement_params_set_EXPECT_get_exception_and_params_stay() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(4)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<CommandException>(result)
        assertEquals(4, refuelableObjectAdapter.getFuel())
        assertEquals(Point(12, 5), movingObjectAdapter.getLocation())
    }


    @Test
    fun WHEN_velocity_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_angle_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
            setProperty(Property.VELOCITY, 8)
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_fuel_consumption_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_fuel_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)
        movingObjectAdapter.setLocation(Point(12, 5))

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_location_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)
        val movingObjectAdapter = MovingObjectAdapter(spaceShip)

        // Act
        val result = runCatching {
            MoveWithFuelConsumptionCommand(movingObjectAdapter, refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
}
