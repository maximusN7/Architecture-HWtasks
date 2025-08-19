package org.example.processors.registered

import org.example.core.utils.InMenuStateUnregistered
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class LogoutProgramCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("logged out")
        return InMenuStateUnregistered()
    }
}
