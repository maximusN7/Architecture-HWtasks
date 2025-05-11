package org.example.hw5_ioc.ioc

import org.example.hw5_ioc.utils.ILambda

class Scope(
    initialStore: MutableMap<String, ILambda> = mutableMapOf(),
    private val strategyIfMissing: ILambda,
) : IScope {

    override var store: MutableMap<String, ILambda> = initialStore

    override fun resolve(key: String, params: List<Any?>?): Any? {
        return store[key]?.invoke(key, params) ?: strategyIfMissing(key, params)
    }
}
