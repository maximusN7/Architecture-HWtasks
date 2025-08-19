package org.example.utils

import org.example.contract.ICommand
import ioc.ioc.IScope
import ioc.ioc.IoC
import org.example.model.Message.Companion.parseToMessage
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import javax.inject.Inject

class ServerLooper @Inject constructor(
    private val kafkaConsumer: KafkaConsumer
) {

    private val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

    fun startProcess() {
        kafkaConsumer.instance.subscribe(listOf("command"))

        val baseScope = IoC.resolve("Scopes.Current") as IScope

        println("Start to listen topic")

        val gameServers = ServerThread(commandQueue)
        val consumerServer = ServerThread(commandQueue)

        gameServers.updateBehaviour {
            var command: ICommand? = null
            try {
                command = if (GameScheduler.hasWork()) {
                    commandQueue.poll()
                } else {
                    commandQueue.take()
                }
                command?.invoke()

                GameScheduler.execute()
            } catch (e: Exception) {
                (IoC.resolve("ExceptionHandler", listOf(command, e)) as ICommand?)?.invoke()
            }
        }
        consumerServer.updateBehaviour {
            try {
                val records = kafkaConsumer.instance.poll(Duration.ofMillis(1000))
                for (record in records) {
                    val message = record.value().toString().parseToMessage()

                    if (validateUsersAccess(message)) {
                        commandQueue.add(InterpreterCommand(message, baseScope, gameServers, consumerServer))
                    }
                }
            } catch (e: Exception) {
                (IoC.resolve("ExceptionHandler", listOf(null, e)) as ICommand?)?.invoke()
            }
        }

        consumerServer.start()
        gameServers.start()
    }
}
