package org.example.hw13_interpreter_pattern

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.example.hw10_microservice_architecture.UsersAccessValidator
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.UObject
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.commands.GameCommand
import org.example.hw8_message_broadcast_systems.model.Message
import org.example.hw8_message_broadcast_systems.setup.CommandsList

class InterpreterCommand(
    private val message: Message,
    private val baseScope: IScope,
    private val gameThread: ServerThread,
    private val serverThread: ServerThread
) : ICommand {

    override fun invoke() {
        // Game Command or Common Command
        if (message.gameId != null) {
            val game = IoC.resolve("GameCommand.Get", listOf(message.gameId, baseScope)) as GameCommand
            val userName = UsersAccessValidator.getUsername(message)

            val uObject = if (userName != null) {
                game.switchToUserScope(userName)
                IoC.resolve("GameObject.Get", listOf(message.gameObjectId)) as UObject
            } else {
                null
            }
            val command = CommandsList.getCommandById(message.operationId, uObject, gameThread, message.args)

            game.gameQueue.add(command)
        } else {
            val command = CommandsList.getCommandById(message.operationId, null, serverThread, message.args)

            serverThread.commandQueue.add(command)
        }
    }
}
