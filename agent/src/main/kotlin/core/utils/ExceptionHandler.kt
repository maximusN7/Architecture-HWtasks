package org.example.core.utils

import org.example.contract.ICommandProcessor
import org.example.di.AppInjector

object ExceptionHandler {

    private var store: MutableMap<Pair<Class<out ICommandProcessor>, Class<out Exception>>, () -> ICommandProcessor> =
        mutableMapOf()

    fun handle(commandProcessor: ICommandProcessor, exception: Exception?): ICommandProcessor {
        println("Handle error $exception")
        exception ?: return AppInjector.component.getLogExceptionProcessor()

        val commandType = commandProcessor::class.java
        val exceptionType = exception::class.java

        val handlerCommand = store[Pair(commandType, exceptionType)] ?: store[Pair(commandType, Exception::class.java)]

        return handlerCommand?.invoke() ?: AppInjector.component.getLogExceptionProcessor()
    }

    fun register(
        commandType: Class<out ICommandProcessor>,
        exceptionType: Class<out Exception>,
        handler: () -> ICommandProcessor,
    ) {
        store[Pair(commandType, exceptionType)] = handler
    }
}
