package org.example.hw8_message_broadcast_systems

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.example.hw10_microservice_architecture.validateUsersAccess
import org.example.hw13_interpreter_pattern.InterpreterCommand
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.model.Message.Companion.parseToMessage
import org.example.hw8_message_broadcast_systems.setup.commandsConsumerProps
import org.example.hw8_message_broadcast_systems.setup.commandsConsumerTopics
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

object ConsumerEndpoint {

    private val commandQueue: BlockingQueue<ICommand?> = ArrayBlockingQueue(100)

    fun startObserving() {
        val consumer = KafkaConsumer<String, String>(commandsConsumerProps)
        consumer.subscribe(commandsConsumerTopics)

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
                val records = consumer.poll(Duration.ofMillis(1000))
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
