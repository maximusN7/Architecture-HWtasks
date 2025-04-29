package org.example.hw2_exceptionhandler

import org.example.hw2_exceptionhandler.contract.ICommand
import java.util.*

object EventLooper {

    val commandQueue: Queue<ICommand> = LinkedList()

    fun processCommands() {
        while (commandQueue.isNotEmpty()) {
            val command = commandQueue.poll()
            try {
                command.invoke()
            } catch (e: Exception) {
                ExceptionHandler.handle(command, e)?.invoke()
            }
        }
    }

    fun addCommands(commands: Queue<ICommand>) {
        commandQueue.addAll(commands)
    }
}
