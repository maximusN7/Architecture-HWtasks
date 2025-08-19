package org.example.processors

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor

class MacroCommandProcessor(
    private val commandProcessors: List<ICommandProcessor>
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        var newState = currentState
        for (processor in commandProcessors) {
            newState = processor.process(newState, parts)
        }
        return newState
    }
}
