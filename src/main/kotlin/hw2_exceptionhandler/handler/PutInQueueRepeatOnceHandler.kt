package org.example.hw2_exceptionhandler.handler

import org.example.hw2_exceptionhandler.command.PutInQueueCommand
import org.example.hw2_exceptionhandler.command.RepeatOnceCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw2_exceptionhandler.contract.IHandler

object PutInQueueRepeatOnceHandler : IHandler {

    override fun handle(command: ICommand, exception: Exception): ICommand {
        return PutInQueueCommand(RepeatOnceCommand(command))
    }
}
