package org.example.hw11_state_pattern

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.example.hw2_exceptionhandler.contract.ICommand
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class CommandLoopWithState {

    internal val commandQueueOne: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)
    internal val commandQueueTwo: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

    internal val servers: MutableList<ServerThread> = mutableListOf()

    internal val states: MutableList<QueueHandlerState?> = mutableListOf(
        DefaultState(commandQueueOne, commandQueueTwo),
        DefaultState(commandQueueTwo, commandQueueOne),
    )

    fun startObserving() {
        servers.forEachIndexed { index, server ->
            server.updateBehaviour {
                println("current state: ${states[index]}")
                states[index] = states[index]?.handle()
            }
        }

        servers.forEach { it.start() }
    }
}
