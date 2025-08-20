package org.example.exceptions

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class LogExceptionCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("Something went wrong. Try again later")
        return currentState
    }
}
