package org.example.core.utils

import org.example.contract.IClientState
import org.example.contract.ICommand
import org.example.contract.IInputInterpreter
import org.example.interpreters.GameInputInterpreter
import org.example.interpreters.MenuInputRegisteredInterpreter
import org.example.interpreters.MenuInputUnregisteredInterpreter
import org.example.model.Token
import org.example.model.UserData

abstract class ConsoleCommandsClientState : IClientState {

    protected abstract val inputInterpreter: IInputInterpreter

    override fun handle(command: ICommand?): IClientState? {
        val stringCommand = (command as? ConsoleStringCommand)?.commandString ?: return null

        return inputInterpreter.interpret(stringCommand)
    }
}

class InGameState(
    val currentGameId: Long,
    val currentUser: UserData,
    val userToken: Token,
) : ConsoleCommandsClientState() {

    override val inputInterpreter = GameInputInterpreter(this)
}

class InMenuStateUnregistered : ConsoleCommandsClientState() {

    override val inputInterpreter = MenuInputUnregisteredInterpreter(this)
}

class InMenuStateRegistered(
    val currentUser: UserData,
) : ConsoleCommandsClientState() {

    override val inputInterpreter = MenuInputRegisteredInterpreter(this)
}
