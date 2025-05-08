package org.example.hw3_abstractions.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.contract.IMovingObject
import java.awt.Point

class MoveCommand(
    private val movingObject: IMovingObject,
) : ICommand {

    override fun invoke() {
        movingObject.setLocation(
            Point(
                movingObject.getLocation().x + movingObject.getVelocity().first,
                movingObject.getLocation().y + movingObject.getVelocity().second,
            )
        )
    }
}
