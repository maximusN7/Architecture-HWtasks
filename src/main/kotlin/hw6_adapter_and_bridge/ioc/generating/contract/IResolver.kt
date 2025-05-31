package org.example.hw6_adapter_and_bridge.ioc.generating.contract

import org.example.hw3_abstractions.UObject

interface IResolver {

    fun resolve(uObject: UObject, params: List<Any?>?): Any
}
