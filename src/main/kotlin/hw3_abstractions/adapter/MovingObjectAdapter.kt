package org.example.hw3_abstractions.adapter

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.contract.IMovingObject
import org.example.hw3_abstractions.model.Angle
import java.awt.Point
import kotlin.math.cos
import kotlin.math.sin

class MovingObjectAdapter(
    private val obj: UObject,
) : IMovingObject {

    override fun getLocation(): Point {
        return obj.getProperty(Property.LOCATION) as Point
    }

    override fun setLocation(newPoint: Point) {
        obj.setProperty(Property.LOCATION, newPoint)
    }

    override fun getVelocity(): Pair<Int, Int> {
        val velocity = obj.getProperty(Property.VELOCITY) as Int
        val angle = obj.getProperty(Property.ANGLE) as Angle

        return Pair(
            (velocity * cos(angle.getAngleRadians())).toInt(),
            (velocity * sin(angle.getAngleRadians())).toInt(),
        )
    }
}
