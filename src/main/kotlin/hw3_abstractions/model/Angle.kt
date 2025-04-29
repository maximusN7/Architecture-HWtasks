package org.example.hw3_abstractions.model

data class Angle(
    var d: Byte,
    var n: Byte,
) {

    fun getAngleRadians(): Double {
        return Math.toRadians(360.0 * d / n)
    }
}
