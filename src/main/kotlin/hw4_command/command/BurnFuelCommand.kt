package org.example.hw4_command.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw4_command.contract.IRefuelableObject

class BurnFuelCommand(
    private val refuelableObject: IRefuelableObject
) : ICommand {

    override fun invoke() {
        refuelableObject.setFuel(refuelableObject.getFuel() - refuelableObject.getFuelConsumptionSpeed())
    }
}
