package org.example.hw12_chain_of_responsibility_pattern

import java.awt.Point

interface IGameFieldObject {

    fun getIsCollided(): Boolean

    fun setIsCollided(newState: Boolean)

    fun setLocation(newPoint: Point)

    fun getLocation(): Point
}
