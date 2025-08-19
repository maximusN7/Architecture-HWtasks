package org.example.contract

import org.example.model.Angle

interface IRotatingObject {

    fun setDirection(newDirection: Angle)

    fun getAngularVelocity(): Byte

    fun getAngle(): Angle
}
