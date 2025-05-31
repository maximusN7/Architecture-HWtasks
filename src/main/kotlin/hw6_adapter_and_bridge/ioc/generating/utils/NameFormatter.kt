package org.example.hw6_adapter_and_bridge.ioc.generating.utils

import kotlin.reflect.KClass

object NameFormatter {

    fun KClass<*>.getAdapterName(): String {
        val interfaceName = this.simpleName ?: throw NoSuchElementException("Unknown interface $this")

        return StringBuilder(interfaceName.substringAfter("I")).append("Adapter").toString()
    }

    fun KClass<*>.getFactoryName(): String {
        return StringBuilder(this.getAdapterName()).append("Factory").toString()
    }

    fun KClass<*>.getPluginName(): String {
        return StringBuilder(this.getAdapterName()).append("Plugin").toString()
    }
}
