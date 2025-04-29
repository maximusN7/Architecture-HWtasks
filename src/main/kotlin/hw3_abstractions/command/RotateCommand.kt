package org.example.hw3_abstractions.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.contract.IRotatingObject
import org.example.hw3_abstractions.model.Angle

class RotateCommand(
    private val rotatingObject: IRotatingObject
) : ICommand {

    override fun invoke() {
        val currentAngle = rotatingObject.getAngle()
        rotatingObject.setDirection(
            Angle(
                (currentAngle.d + rotatingObject.getAngularVelocity()).toByte(),
                currentAngle.n,
            )
        )
    }
}
