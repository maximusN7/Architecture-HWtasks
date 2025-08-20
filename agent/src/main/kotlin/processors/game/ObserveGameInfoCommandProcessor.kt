package org.example.processors.game

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.core.utils.GameStateListener
import org.example.core.utils.InGameState
import javax.inject.Inject

class ObserveGameInfoCommandProcessor @Inject constructor(
    private val gameStateListener: GameStateListener
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        gameStateListener.startObserve((currentState as InGameState).currentGameId)

        return currentState
    }
}
