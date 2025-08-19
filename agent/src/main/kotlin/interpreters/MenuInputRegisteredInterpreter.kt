package org.example.interpreters

import org.example.core.utils.ExceptionHandler
import org.example.contract.IInputInterpreter
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.di.AppInjector
import org.example.processors.MacroCommandProcessor

class MenuInputRegisteredInterpreter(private val currentState: IClientState?) : IInputInterpreter {

    override fun interpret(command: String): IClientState? {
        val parts = command.split(" ")

        val commandProcessor = getProcessor(parts[0])
        return try {
            commandProcessor.process(currentState, parts)
        } catch (e: Exception) {
            println("Exception $e")
            ExceptionHandler.handle(commandProcessor, e).process(currentState, parts)
        }
    }

    private fun getProcessor(commandString: String): ICommandProcessor {
        return when (commandString) {
            "create" -> {
                AppInjector.component.getCreateGameProcessor()
            }

            "join" -> {
                MacroCommandProcessor(
                    listOf(
                        AppInjector.component.getJoinGameProcessor(),
                        AppInjector.component.getObserveGameInfoProcessor(),
                    )
                )
            }

            "logout" -> {
                AppInjector.component.getLogoutProgramProcessor()
            }

            "help" -> {
                AppInjector.component.getHelpForRegisteredProcessor()
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
