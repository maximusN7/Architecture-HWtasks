package org.example.hw5_ioc.ioc.command

import org.example.contract.ICommand
import org.example.hw5_ioc.ioc.strategy.ScopeBasedResolveDependencyStrategy
import ioc.utils.ILambda

class RegisterIoCDependencyCommand(
    private val key: String,
    private val strategy: ILambda,
) : ICommand {

    override fun invoke() {
        ScopeBasedResolveDependencyStrategy.getCurrentScope()?.store?.set(key, strategy)
    }
}
