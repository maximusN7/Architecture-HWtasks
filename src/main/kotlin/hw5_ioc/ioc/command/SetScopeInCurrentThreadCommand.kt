package org.example.hw5_ioc.ioc.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.ioc.strategy.ScopeBasedResolveDependencyStrategy
import org.example.hw5_ioc.ioc.IScope

class SetScopeInCurrentThreadCommand(
    private val scope: IScope,
) : ICommand {

    override fun invoke() {
        ScopeBasedResolveDependencyStrategy.setCurrentScope(scope)
    }
}
