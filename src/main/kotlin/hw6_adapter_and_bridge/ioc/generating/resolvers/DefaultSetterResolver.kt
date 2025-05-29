package org.example.hw6_adapter_and_bridge.ioc.generating.resolvers

import org.example.hw3_abstractions.UObject
import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver

class DefaultSetterResolver : IResolver {

    override fun resolve(uObject: UObject, params: List<Any?>?): Any {
        return uObject.setProperty(params?.get(0) as String, params[1])
    }
}
