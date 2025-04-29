package org.example.hw4_command.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw4_command.contract.IRefuelableObject
import org.example.hw4_command.model.CommandException

class CheckFuelCommand(
    private val refuelableObject: IRefuelableObject
) : ICommand {

    override fun invoke() {
        if (refuelableObject.getFuel() - refuelableObject.getFuelConsumptionSpeed() < 0) {
            throw CommandException("not enough fuel")
        }
    }
}
