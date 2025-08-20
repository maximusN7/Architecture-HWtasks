package org.example.model

data class Angle(
    var d: Byte,
    var n: Byte,
) {

    fun getAngleRadians(): Double {
        return Math.toRadians(360.0 * d / n)
    }

    fun getAngleRDegrees(): Int {
        return (360.0 * d / n).toInt()
    }
}
