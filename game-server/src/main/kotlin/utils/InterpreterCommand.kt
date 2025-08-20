package org.example.utils

import org.example.command.GameCommand
import org.example.contract.ICommand
import org.example.contract.IMovingObject
import ioc.ioc.IScope
import ioc.ioc.IoC
import org.example.model.Message
import org.example.model.Property
import org.example.model.UObject
import org.example.utils.setup.CommandsList

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
