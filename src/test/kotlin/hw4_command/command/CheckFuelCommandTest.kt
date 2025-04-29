package hw4_command.command

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw4_command.adapter.RefuelableObjectAdapter
import org.example.hw4_command.command.CheckFuelCommand
import org.example.hw4_command.model.CommandException
import org.junit.jupiter.api.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CheckFuelCommandTest {

    @Test
    fun WHEN_fuel_consumption_is_more_than_fuel_amount_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(4)

        // Act
        val result = runCatching {
            CheckFuelCommand(refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<CommandException>(result)
    }

    @Test
    fun WHEN_fuel_consumption_is_less_than_fuel_amount_EXPECT_get_no_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 5)
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)

        // Act
        val result = runCatching {
            CheckFuelCommand(refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNull(result)
    }

    @Test
    fun WHEN_fuel_consumption_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip()
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)

        // Act
        val result = runCatching {
            CheckFuelCommand(refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }

    @Test
    fun WHEN_initial_fuel_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 2)
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)

        // Act
        val result = runCatching {
            CheckFuelCommand(refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
}
