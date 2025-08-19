package org.example.command

import org.example.contract.ICommand
import org.example.contract.IMovingObject
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
