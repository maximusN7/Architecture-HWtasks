package hw4_command.command

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw4_command.adapter.RefuelableObjectAdapter
import org.example.hw4_command.command.BurnFuelCommand
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BurnFuelCommandTest {

    @Test
    fun WHEN_fuel_consumption_is_2_and_initial_fuel_is_10_EXPECT_fuel_become_8() {
        // Arrange
        val spaceShip = SpaceShip().apply {
            setProperty(Property.FUEL_CONSUMPTION, 2)
        }
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)

        // Act
        BurnFuelCommand(refuelableObjectAdapter).invoke()

        // Assert
        assertEquals(8, refuelableObjectAdapter.getFuel())
    }

    @Test
    fun WHEN_fuel_consumption_is_not_set_EXPECT_get_exception() {
        // Arrange
        val spaceShip = SpaceShip()
        val refuelableObjectAdapter = RefuelableObjectAdapter(spaceShip)
        refuelableObjectAdapter.setFuel(10)

        // Act
        val result = runCatching {
            BurnFuelCommand(refuelableObjectAdapter).invoke()
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
            BurnFuelCommand(refuelableObjectAdapter).invoke()
        }.exceptionOrNull()

        // Assert
        assertNotNull(result)
        assertIs<NoSuchElementException>(result)
    }
}
