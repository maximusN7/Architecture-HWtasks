package org.example.hw4_command.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.command.MoveCommand
import org.example.hw3_abstractions.contract.IMovingObject
import org.example.hw4_command.contract.IRefuelableObject
import java.util.*

class MoveWithFuelConsumptionCommand(
    private val movingObject: IMovingObject,
    private val refuelableObject: IRefuelableObject,
) : ICommand {

    override fun invoke() {
        val commands = LinkedList<ICommand>().apply {
            add(CheckFuelCommand(refuelableObject))
            add(MoveCommand(movingObject))
            add(BurnFuelCommand(refuelableObject))
        }
        MacroCommand(commands).invoke()
    }
}
