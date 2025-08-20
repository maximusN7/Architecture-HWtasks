package org.example.contract

import java.awt.Point

interface IMovingObject {

    fun getLocation(): Point

    fun setLocation(newPoint: Point)

    fun getVelocity(): Pair<Int, Int>
}
