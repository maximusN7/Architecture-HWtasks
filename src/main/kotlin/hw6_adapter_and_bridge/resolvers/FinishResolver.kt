package org.example.hw6_adapter_and_bridge.resolvers

import org.example.hw3_abstractions.UObject
import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver

class FinishResolver : IResolver {

    override fun resolve(uObject: UObject, params: List<Any?>?): Any {
        return println("Something is finished")
    }
}
