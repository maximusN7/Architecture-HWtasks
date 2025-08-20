package org.example.core.utils

import org.example.contract.ICommand
import org.example.contract.ICommandProducer
import javax.inject.Inject

class ConsoleCommandProducer @Inject constructor() : ICommandProducer {

    override fun getCommand(): ICommand {
        val input = readlnOrNull()
        return ConsoleStringCommand(input)
    }
}
