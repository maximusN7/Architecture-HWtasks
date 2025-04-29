package org.example.hw4_command.command

import org.example.hw2_exceptionhandler.contract.ICommand
import java.util.Queue

class MacroCommand(
    private val commands: Queue<ICommand>
) : ICommand {

    override fun invoke() {
        while (commands.isNotEmpty()) {
            val command = commands.poll()
            command.invoke()
        }
    }
}
