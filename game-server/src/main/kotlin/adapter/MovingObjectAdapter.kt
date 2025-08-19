package org.example.adapter

import org.example.contract.IMovingObject
import org.example.model.Angle
import org.example.model.Property
import org.example.model.UObject
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
