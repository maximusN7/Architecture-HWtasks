package org.example.hw5_ioc.ioc

import org.example.hw5_ioc.utils.ILambda

interface IScope {

    var store: MutableMap<String, ILambda>

    fun resolve(key: String, params: List<Any?>? = null): Any?
}
