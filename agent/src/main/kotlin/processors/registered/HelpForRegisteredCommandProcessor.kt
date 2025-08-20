package org.example.processors.registered

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class HelpForRegisteredCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("""
            You are currently signed in.
            - To create game type: create <gameName> <participants names separated with space>
            - To join game type: join <your password> <gameId>
            - To logout type: logout
            - To exit type: exit
        """.trimIndent())
        return currentState
    }
}
