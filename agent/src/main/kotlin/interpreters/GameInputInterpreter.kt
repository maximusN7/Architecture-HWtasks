package org.example.interpreters

import org.example.core.utils.ExceptionHandler
import org.example.contract.IInputInterpreter
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.di.AppInjector
import org.example.processors.MacroCommandProcessor

class GameInputInterpreter(private val currentState: IClientState) : IInputInterpreter {

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
            "help" -> {
                AppInjector.component.getHelpInGameProcessor()
            }

            "exit" -> {
                MacroCommandProcessor(
                    listOf(
                        AppInjector.component.getExitToMenuProcessor(),
                        AppInjector.component.getStopObserveGameInfoProcessor()
                    )
                )
            }

            "move" -> {
                AppInjector.component.getMoveProcessor()
            }

            "rotate" -> {
                AppInjector.component.getRotateProcessor()
            }

            "shoot" -> {
                AppInjector.component.getShootProcessor()
            }

            else -> {
                AppInjector.component.getUnknownProcessor()
            }
        }
    }
}
