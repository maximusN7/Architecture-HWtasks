package org.example.hw5_ioc.ioc

import org.example.hw5_ioc.utils.ILambda

class Scope(
    initialStore: MutableMap<String, ILambda> = mutableMapOf(),
    private val strategyIfMissing: ILambda,
) : IScope {

    override var store: MutableMap<String, ILambda> = initialStore

    override fun <T> resolve(key: String?, params: List<Any?>?): T {
        key ?: throw IllegalArgumentException("key is null")

        return store[key]?.invoke(key, params) as? T ?: strategyIfMissing(key, params) as T
    }
}
