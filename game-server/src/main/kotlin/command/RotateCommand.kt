package org.example.command

import org.example.contract.ICommand
import org.example.contract.IRotatingObject
import org.example.model.Angle

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
