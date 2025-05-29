package org.example.hw3_abstractions.contract

import hw6_adapter_and_bridge.resolvers.DirectionResolver
import org.example.hw3_abstractions.model.Angle
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.IoCResolver

interface IRotatingObject {

    @IoCResolver(DirectionResolver::class)
    fun setDirection(newDirection: Angle)

    fun getAngularVelocity(): Byte

    fun getAngle(): Angle
}
