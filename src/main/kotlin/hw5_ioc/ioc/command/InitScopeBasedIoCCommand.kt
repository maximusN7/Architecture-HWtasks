package org.example.hw5_ioc.ioc.command

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.ioc.strategy.ScopeBasedResolveDependencyStrategy
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.ioc.Scope
import org.example.hw5_ioc.utils.ILambda
import org.example.hw5_ioc.utils.ResolveDependencyException

class InitScopeBasedIoCCommand(
    private val defaultMissingStrategy: ILambda = ILambda { _, _ -> }
) : ICommand {

    override fun invoke() {
        val store: MutableMap<String, ILambda> = mutableMapOf()
        val scope = Scope(store, defaultMissingStrategy)

        store["Scopes.Root"] = ILambda { _, _ ->
            ScopeBasedResolveDependencyStrategy.root
        }
        store["Scopes.Storage"] = ILambda { _, _ ->
            mutableMapOf<String, ILambda>()
        }
        store["Scopes.New"] = ILambda { _, params ->
            scopesNewStrategy(params ?: emptyList())
        }
        store["Scopes.Current"] = ILambda { _, _ ->
            ScopeBasedResolveDependencyStrategy.getCurrentScope()
        }
        store["Scopes.Current.Set"] = ILambda { _, params ->
            SetScopeInCurrentThreadCommand(params?.get(0) as IScope).invoke()
        }
        store["IoC.Register"] = ILambda { _, params ->
            RegisterIoCDependencyCommand(params?.get(0) as String, params[1] as ILambda).invoke()
        }

        ScopeBasedResolveDependencyStrategy.root = scope
        IoC.resolve(
            "IoC.SetupStrategy",
            ILambda { key, params ->
                ScopeBasedResolveDependencyStrategy.resolve(key, params)
            }
        )
        SetScopeInCurrentThreadCommand(scope).invoke()
    }

    private fun scopesNewStrategy(params: List<Any?>): IScope {
        val store = IoC.resolve("Scopes.Storage") as? MutableMap<String, ILambda> ?: mutableMapOf()
        if (params.isEmpty()) return Scope(store) { _, _ -> throw ResolveDependencyException() }

        val parent = params[0]
        if (parent is IScope) {
            return Scope(store) { key, args -> parent.resolve(key, args) }
        }

        if (parent is ILambda) {
            return Scope(store, parent)
        }

        throw Exception("Failed to create scope")
    }
}
