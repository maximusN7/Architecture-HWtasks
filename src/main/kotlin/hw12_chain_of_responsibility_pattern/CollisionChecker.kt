package org.example.hw12_chain_of_responsibility_pattern

import java.awt.Point

object CollisionChecker {

    fun checkIsCollision(currentObject: IGameFieldObject, anotherObject: IGameFieldObject): Boolean {
        return currentObject.getLocation() == anotherObject.getLocation()
    }

    fun checkIsCollisionForThreeDot(currentObject: IGameFieldObject, anotherObject: IGameFieldObject): Boolean {
        val currentCenterLocation = currentObject.getLocation()
        val anotherCenterLocation = anotherObject.getLocation()
        if (currentCenterLocation.y == anotherCenterLocation.y) {
            return when {
                isInRange(currentCenterLocation.x - 1, anotherCenterLocation) -> {
                    true
                }
                isInRange(currentCenterLocation.x, anotherCenterLocation) -> {
                    true
                }
                isInRange(currentCenterLocation.x + 1, anotherCenterLocation) -> {
                    true
                }
                else -> false
            }
        }
        return false
    }

    private fun isInRange(x: Int, anotherCenterLocation: Point): Boolean {
        return x <= anotherCenterLocation.x + 1 && x >= anotherCenterLocation.x - 1
    }
}
