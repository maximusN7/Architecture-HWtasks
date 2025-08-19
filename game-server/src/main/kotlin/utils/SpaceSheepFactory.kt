package org.example.utils

import org.example.model.Angle
import org.example.model.Property
import org.example.model.SpaceShip
import org.jetbrains.kotlin.javax.inject.Inject
import java.awt.Point

class SpaceSheepFactory @Inject constructor() {

    fun getSpaceSheep(initialPosition: Point): SpaceShip {
        return SpaceShip().apply {
            setProperty(Property.LOCATION, initialPosition)
            setProperty(Property.VELOCITY, 0)
            setProperty(Property.ANGLE, Angle(0, 7))
            setProperty(Property.ANGULAR_VELOCITY, 0.toByte())
        }
    }
}
