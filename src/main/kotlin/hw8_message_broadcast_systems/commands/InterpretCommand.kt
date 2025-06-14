package org.example.hw8_message_broadcast_systems.commands

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.setup.CommandsList
import org.example.hw8_message_broadcast_systems.model.Message

class InterpretCommand(
    private val message: Message,
    private val baseScope: IScope,
    private val thread: ServerThread
) : ICommand {

    override fun invoke() {
        val game = IoC.resolve("GameCommand.Get", listOf(message.gameId, baseScope)) as GameCommand
        val uObject = game.getObjectById(message.gameObjectId, message.args)
        val command = CommandsList.getCommandById(message.operationId, uObject, thread, message.args)

        game.gameQueue.add(command)
    }
}
