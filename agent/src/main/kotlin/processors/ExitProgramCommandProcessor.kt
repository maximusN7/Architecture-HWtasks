package org.example.processors

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class ExitProgramCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("Exit...")
        return null
    }
}
