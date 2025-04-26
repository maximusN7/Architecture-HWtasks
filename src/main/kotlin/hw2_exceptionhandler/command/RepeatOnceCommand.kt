package org.example.hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.contract.ICommand

class RepeatOnceCommand(
    private val command: ICommand
) : ICommand {

    override fun invoke() {
        println("repeat command ${command::class.simpleName}")
        command.invoke()
    }
}
