package org.example.processors.game

import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import javax.inject.Inject

class ExitToMenuCommandProcessor @Inject constructor() : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        println("Exit to menu...")
        return InMenuStateRegistered(
            currentUser = (currentState as InGameState).currentUser
        )
    }
}
