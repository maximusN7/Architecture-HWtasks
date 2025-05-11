package org.example.hw5_ioc.ioc.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.ioc.strategy.ScopeBasedResolveDependencyStrategy
import org.example.hw5_ioc.utils.ILambda

class RegisterIoCDependencyCommand(
    private val key: String,
    private val strategy: ILambda,
) : ICommand {

    override fun invoke() {
        ScopeBasedResolveDependencyStrategy.getCurrentScope()?.store?.set(key, strategy)
    }
}
