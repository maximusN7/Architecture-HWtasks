package org.example.hw11_state_pattern

import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import org.example.hw11_state_pattern.commands.MoveToCommand
import org.example.hw11_state_pattern.commands.RunCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import java.util.concurrent.BlockingQueue

abstract class QueueHandlerState(
    val commandQueue: BlockingQueue<ICommand?>,
    val redirectCommandQueue: BlockingQueue<ICommand?>,
) {
    abstract fun handle(): QueueHandlerState?
}

class DefaultState(
    commandQueue: BlockingQueue<ICommand?>,
    redirectCommandQueue: BlockingQueue<ICommand?>,
) : QueueHandlerState(commandQueue, redirectCommandQueue) {

    override fun handle(): QueueHandlerState? {
        val command = commandQueue.take()
        command?.invoke()

        return when (command) {
            is HardStopCommand -> null
            is MoveToCommand -> MoveToState(commandQueue, redirectCommandQueue)
            else -> this
        }
    }
}

class MoveToState(
    commandQueue: BlockingQueue<ICommand?>,
    redirectCommandQueue: BlockingQueue<ICommand?>,
) : QueueHandlerState(commandQueue, redirectCommandQueue) {

    override fun handle(): QueueHandlerState? {
        val command = commandQueue.take()
        if (command != null) {
            redirectCommandQueue.add(command)
        }

        return when (command) {
            is HardStopCommand -> null
            is RunCommand -> DefaultState(commandQueue, redirectCommandQueue)
            else -> this
        }
    }
}
