package org.example.hw5_ioc.ioc

import org.example.hw5_ioc.utils.ILambda

interface IScope {

    var store: MutableMap<String, ILambda>

    fun <T> resolve(key: String?, params: List<Any?>? = null): T?
}
