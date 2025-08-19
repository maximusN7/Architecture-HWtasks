package org.example.interpreters

import org.example.core.utils.ExceptionHandler
import org.example.contract.IInputInterpreter
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.di.AppInjector

class MenuInputUnregisteredInterpreter(private val currentState: IClientState?) : IInputInterpreter {

    override fun interpret(command: String): IClientState? {
        val parts = command.split(" ")

        val commandProcessor = getProcessor(parts[0])
        return try {
            commandProcessor.process(currentState, parts)
        } catch (e: Exception) {
            ExceptionHandler.handle(commandProcessor, e).process(currentState, parts)
        }
    }

    private fun getProcessor(commandString: String): ICommandProcessor {
        return when (commandString) {
            "join", "create" -> {
                AppInjector.component.getGameFromUnregisteredProcessor()
            }

            "login" -> {
                AppInjector.component.getLoginProcessor()
            }

            "help" -> {
                AppInjector.component.getHelpForUnregisteredProcessor()
            }

            "exit" -> {
                AppInjector.component.getExitProgramProcessor()
            }

            else -> {
                AppInjector.component.getUnknownProcessor()
            }
        }
    }
}
