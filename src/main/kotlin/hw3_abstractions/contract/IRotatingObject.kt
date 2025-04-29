package org.example.hw3_abstractions.contract

import org.example.hw3_abstractions.model.Angle

interface IRotatingObject {

    fun setDirection(newDirection: Angle)

    fun getAngularVelocity(): Byte

    fun getAngle(): Angle
}
