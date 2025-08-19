package org.example.processors.unregistered

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class GameFromUnregisteredCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("You are unregistered. Login before play")
        return currentState
    }
}
