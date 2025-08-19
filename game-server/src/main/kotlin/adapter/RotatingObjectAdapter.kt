package org.example.adapter

import org.example.contract.IRotatingObject
import org.example.model.Angle
import org.example.model.Property
import org.example.model.UObject

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
