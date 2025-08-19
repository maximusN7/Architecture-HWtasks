package ioc.ioc

import ioc.utils.ILambda

interface IScope {

    var store: MutableMap<String, ILambda>

    fun <T> resolve(key: String?, params: List<Any?>? = null): T?
}
