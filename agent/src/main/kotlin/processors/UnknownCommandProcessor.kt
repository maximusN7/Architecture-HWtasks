package org.example.processors

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class UnknownCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("Unknown command. To see available commands type command: help")
        return currentState
    }
}
