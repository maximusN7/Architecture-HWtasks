package org.example.utils

import org.example.contract.ICommand
import ioc.ioc.IoC
import java.util.concurrent.BlockingQueue

class ServerThread(
    internal val commandQueue: BlockingQueue<ICommand?>,
    private val actionBeforeStart: (() -> Unit)? = null,
    private val actionAfter: (() -> Unit)? = null,
) {

    private var stop: Boolean = false
    private var thread: Thread? = null

    private var behaviour: () -> Unit = {
        val command = commandQueue.poll()
        try {
            command?.invoke()
        } catch (e: Exception) {
            (IoC.resolve("ExceptionHandler", listOf(command, e)) as ICommand).invoke()
        }
    }

    init {
        thread = Thread {
            actionBeforeStart?.invoke()
            while (!stop) {
                behaviour.invoke()
            }
            actionAfter?.invoke()
        }
    }

    fun start() {
        thread?.start()
    }

    fun stop() {
        check(thread?.id == Thread.currentThread().id)
        stop = true
        behaviour = {}
    }

    fun updateBehaviour(newBehaviour: () -> Unit) {
        behaviour = newBehaviour
    }

    fun getBehaviour(): () -> Unit {
        return behaviour
    }
}
