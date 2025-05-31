package org.example.hw6_adapter_and_bridge.resolvers

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver
import kotlin.math.cos
import kotlin.math.sin

class VelocityResolver : IResolver {

    override fun resolve(uObject: UObject, params: List<Any?>?): Any {
        val velocity = uObject.getProperty(Property.VELOCITY) as Int
        val angle = uObject.getProperty(Property.ANGLE) as Angle

        return Pair(
            (velocity * cos(angle.getAngleRadians())).toInt(),
            (velocity * sin(angle.getAngleRadians())).toInt(),
        )
    }
}
