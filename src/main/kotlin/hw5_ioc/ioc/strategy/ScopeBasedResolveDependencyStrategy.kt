package org.example.hw5_ioc.ioc.strategy

import org.example.hw5_ioc.ioc.IScope

object ScopeBasedResolveDependencyStrategy {

    var root: IScope? = null
    private val defaultScope: () -> IScope? = { root }
    private val _currentScope = ThreadLocal<IScope?>()

    fun getCurrentScope(): IScope? {
        return _currentScope.get() ?: defaultScope()
    }

    fun setCurrentScope(newScope: IScope) {
        _currentScope.set(newScope)
    }

    fun resolve(key: String, params: List<Any?>?): Any? {
        return getCurrentScope()?.resolve(key, params)
    }
}
