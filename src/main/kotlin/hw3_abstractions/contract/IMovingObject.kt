package org.example.hw3_abstractions.contract

import org.example.hw6_adapter_and_bridge.ioc.generating.utils.IoCResolver
import org.example.hw6_adapter_and_bridge.resolvers.FinishResolver
import org.example.hw6_adapter_and_bridge.resolvers.VelocityResolver
import java.awt.Point

interface IMovingObject {

    fun getLocation(): Point

    fun setLocation(newPoint: Point)

    @IoCResolver(VelocityResolver::class)
    fun getVelocity(): Pair<Int, Int>

    @IoCResolver(FinishResolver::class)
    fun finish()
}
