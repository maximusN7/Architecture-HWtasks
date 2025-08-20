package org.example.processors.game

import org.example.core.utils.InGameState
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class HelpInGameCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("""
            You are currently in game ${(currentState as InGameState).currentGameId}
            - To move type: move <velocity>
            - To rotate type: rotate <angular velocity>
            - To shoot type: shoot
            - To exit in menu type: exit
        """.trimIndent())
        return currentState
    }
}
