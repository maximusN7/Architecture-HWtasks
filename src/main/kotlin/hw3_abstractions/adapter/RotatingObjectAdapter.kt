package org.example.hw3_abstractions.adapter

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.contract.IRotatingObject
import org.example.hw3_abstractions.model.Angle

class RotatingObjectAdapter(
    private val obj: UObject,
) : IRotatingObject {

    override fun setDirection(newDirection: Angle) {
        return obj.setProperty(Property.ANGLE, newDirection)
    }

    override fun getAngularVelocity(): Byte {
        return obj.getProperty(Property.ANGULAR_VELOCITY) as Byte
    }

    override fun getAngle(): Angle {
        return obj.getProperty(Property.ANGLE) as Angle
    }
}
