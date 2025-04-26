package org.example.hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.EventLooper
import org.example.hw2_exceptionhandler.contract.ICommand

class PutInQueueCommand(
    private val command: ICommand,
) : ICommand {

    override fun invoke() {
        EventLooper.commandQueue.add(command)
    }
}
