package org.example.core

import org.example.core.utils.InMenuStateUnregistered
import org.example.contract.IClientState
import org.example.contract.ICommandProducer
import javax.inject.Inject

class ClientLooper @Inject constructor(
    private val commandProducer: ICommandProducer,
) {

    private var state: IClientState? = InMenuStateUnregistered()

    fun startProcess() {
        while (true) {
            val command = commandProducer.getCommand()
            state = state?.handle(command) ?: break
        }
    }
}
