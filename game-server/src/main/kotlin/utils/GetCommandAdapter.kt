package org.example.utils

import org.example.adapter.MovingObjectAdapter
import org.example.adapter.RotatingObjectAdapter
import org.example.adapter.ShootingObjectAdapter
import org.example.contract.IMovingObject
import org.example.contract.IRotatingObject
import org.example.contract.IShootingObject
import org.example.model.UObject
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class GetCommandAdapter(
    private val kInterface: KClass<out Any>,
    private val uObject: UObject,
) {

    private val adapters: Map<KClass<out Any>, KClass<out Any>> = mapOf(
        IMovingObject::class to MovingObjectAdapter::class,
        IRotatingObject::class to RotatingObjectAdapter::class,
        IShootingObject::class to ShootingObjectAdapter::class,
    )

    fun getAdapter(): Any? {
        return adapters[kInterface]?.primaryConstructor?.call(uObject)
    }
}
