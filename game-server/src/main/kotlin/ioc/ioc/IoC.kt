package ioc.ioc

import org.example.contract.ICommand
import ioc.utils.ILambda
import ioc.utils.ResolveDependencyException
import org.jetbrains.annotations.TestOnly

object IoC {

    private val defaultStrategy = ILambda { key, params ->
        when (key) {
            "IoC.SetupStrategy" -> {
                SetupStrategyCommand(params?.get(0) as ILambda).invoke()
            }

            else -> {
                throw ResolveDependencyException(key)
            }
        }
    }

    private var strategy = defaultStrategy

    fun resolve(key: String?, params: List<Any?>? = null): Any? {
        key ?: throw IllegalArgumentException("key is null")

        return strategy(key, params)
    }

    // syntactic sugar
    fun resolve(key: String?, param: Any?): Any? {
        return resolve(key, listOf(param))
    }

    @TestOnly
    fun clear() {
        strategy = defaultStrategy
    }

    private class SetupStrategyCommand(private val newStrategy: ILambda) : ICommand {

        override fun invoke() {
            strategy = newStrategy
        }
    }
}
