package org.example.hw2_exceptionhandler.handler

import org.example.hw2_exceptionhandler.command.PutInQueueCommand
import org.example.hw2_exceptionhandler.command.RepeatTwiceCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw2_exceptionhandler.contract.IHandler

object PutInQueueRepeatTwiceElseLogHandler : IHandler {

    override fun handle(command: ICommand, exception: Exception): ICommand {
        return PutInQueueCommand(RepeatTwiceCommand(command))
    }
}
