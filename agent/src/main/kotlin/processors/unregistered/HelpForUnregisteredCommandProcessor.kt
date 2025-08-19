package org.example.processors.unregistered

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class HelpForUnregisteredCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("""
            You are currently not signed in.
            - To sign in/sign up type: login <username> <password>
            - To exit type: exit
        """.trimIndent())
        return currentState
    }
}
