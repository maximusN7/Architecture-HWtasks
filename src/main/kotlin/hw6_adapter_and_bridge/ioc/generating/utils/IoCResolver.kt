package org.example.hw6_adapter_and_bridge.ioc.generating.utils

import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver
import kotlin.reflect.KClass

annotation class IoCResolver(val resolver: KClass<out IResolver>)
