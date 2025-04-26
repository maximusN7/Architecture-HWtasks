package org.example.hw2_exceptionhandler

import org.example.hw2_exceptionhandler.contract.ICommand
import kotlin.Exception

object ExceptionHandler {

    private var store: MutableMap<Pair<Class<out ICommand>, Class<out Exception>>, (ICommand, Exception) -> ICommand> =
        mutableMapOf()

    fun handle(command: ICommand, exception: Exception): ICommand? {
        val commandType = command::class.java
        val exceptionType = exception::class.java

        val handlerCommand = store[Pair(commandType, exceptionType)] ?: store[Pair(commandType, Exception::class.java)]

        return handlerCommand?.invoke(command, exception)
    }

    fun register(
        commandType: Class<out ICommand>,
        exceptionType: Class<out Exception>,
        handler: (ICommand, Exception) -> ICommand,
    ) {
        store[Pair(commandType, exceptionType)] = handler
    }
}
