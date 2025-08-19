package org.example.processors

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class AuthErrorExceptionProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("Password is incorrect")
        return currentState
    }
}
