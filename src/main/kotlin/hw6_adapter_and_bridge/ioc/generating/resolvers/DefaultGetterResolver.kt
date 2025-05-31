package org.example.hw6_adapter_and_bridge.ioc.generating.resolvers

import org.example.hw3_abstractions.UObject
import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver

class DefaultGetterResolver : IResolver {

    override fun resolve(uObject: UObject, params: List<Any?>?): Any {
        return uObject.getProperty(params?.get(0) as String)
    }
}
