package org.example.hw6_adapter_and_bridge.ioc.generating.utils

import kotlin.reflect.KCallable

object KCallableUtils {

    fun KCallable<*>.isGetter(): Boolean {
        return this.name.startsWith("get")
    }

    fun KCallable<*>.isSetter(): Boolean {
        return this.name.startsWith("set")
    }

    fun Collection<KCallable<*>>.filterSystemMethods() : List<KCallable<*>> {
        return this.filter { it.name !in listOf("equals", "hashCode", "toString") }
    }
}
