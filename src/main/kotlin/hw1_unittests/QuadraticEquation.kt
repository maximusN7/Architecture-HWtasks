package org.example.hw1_unittests

import kotlin.math.abs
import kotlin.math.sqrt

open class QuadraticEquation {

    fun solve(a: Double, b: Double, c: Double): Array<Double> {
        if (a.isZero()) {
            throw IllegalArgumentException("'a' is required to be not zero")
        }
        if (a.isNaN() || b.isNaN() || c.isNaN()) {
            throw IllegalArgumentException("one of the arguments is NaN")
        }
        if (a.isInfinite() || b.isInfinite() || c.isInfinite()) {
            throw IllegalArgumentException("one of the arguments is Infinite")
        }

        val discriminant = b * b - 4 * a * c

        return when {
            discriminant < -EPSILON -> {
                emptyArray<Double>()
            }

            discriminant.isZero() -> {
                val answer = -b / (2 * a)
                arrayOf(answer, answer)
            }

            discriminant > EPSILON -> {
                val sqrtD = sqrt(discriminant)
                arrayOf((-b + sqrtD) / (2 * a), (-b - sqrtD) / (2 * a))
            }

            else -> throw IllegalArgumentException("Impossible arguments")
        }
    }

    private fun Double.isZero(): Boolean {
        return abs(this) <= EPSILON
    }
}

private const val EPSILON = 1e-10
