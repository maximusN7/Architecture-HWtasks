package org.example.hw5_ioc.ioc

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.utils.ILambda
import org.example.hw5_ioc.utils.ResolveDependencyException

object IoC {

    private var strategy = ILambda { key, params ->
        when (key) {
            "IoC.SetupStrategy" -> {
                SetupStrategyCommand(params?.get(0) as ILambda).invoke()
            }

            else -> {
                throw ResolveDependencyException(key)
            }
        }
    }

    fun resolve(key: String, params: List<Any?>? = null): Any? {
        return strategy(key, params)
    }

    // syntactic sugar
    fun resolve(key: String, param: Any?): Any? {
        return resolve(key, listOf(param))
    }

    private class SetupStrategyCommand(private val newStrategy: ILambda) : ICommand {

        override fun invoke() {
            strategy = newStrategy
        }
    }
}
