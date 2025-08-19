package org.example.processors.game

import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.core.utils.GameStateListener
import javax.inject.Inject

class StopObserveGameInfoCommandProcessor @Inject constructor(
    private val gameStateListener: GameStateListener
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        gameStateListener.stopObserve()

        return currentState
    }
}
